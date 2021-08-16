import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, combineLatest, Observable, of, ReplaySubject, zip} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {FormControl} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {SpecDiffDialogComponent} from "../../components/spec-diff-dialog/spec-diff-dialog.component";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatProductRelease} from "../../services/dashboard/model/boatProductRelease";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {BoatSpec} from "../../services/dashboard/model/boatSpec";
import {BoatCapability} from "../../services/dashboard/model/boatCapability";

export enum ChangeState {
  NEW = 'NEW', DELETED = 'DELETED', CHANGED = 'CHANGED', UNCHANGED = 'UNCHANGED'
}

export class ReleaseSpec {

  constructor(public product: BoatProduct, public spec1: BoatSpec | null, public spec2: BoatSpec | null = null) {
  }

  public key() {
    return this.spec1?.name + ":" + this.spec2?.name;
  }

  public state(): ChangeState {
    if (this.spec1 === null) {
      return ChangeState.NEW
    }
    if (this.spec2 === null) {
      return ChangeState.DELETED
    }
    if (this.spec1?.version === this.spec2?.version) {
      return ChangeState.UNCHANGED
    } else {
      return ChangeState.CHANGED
    }
  }

}

class ReleaseCapability {

  constructor(public boatProduct: BoatProduct, public capability: BoatCapability, public isNew: boolean, public specs: ReleaseSpec[] = []) {
  }

  hasChanges(): boolean {
    return this.specs.some(spec => spec.state() == ChangeState.CHANGED)
  }

  addSpec(spec: BoatSpec) {
    let releaseSpec: ReleaseSpec = new ReleaseSpec(this.boatProduct, spec);
    this.specs.push(releaseSpec);
  }

  compareSpec(newSpec: BoatSpec) {
    const releaseSpec = this.specs.find(value => newSpec.key === value.spec1?.key);
    if (releaseSpec) {
      releaseSpec.spec2 = newSpec
    } else if (!releaseSpec) {
      console.info("something?", newSpec);
      // this.specs.push(new ReleaseSpec(null,newSpec))
    }
  }
}

@Component({
  selector: 'bb-product-dashboard',
  templateUrl: 'diff-dashboard.component.html',
  styleUrls: ['diff-dashboard.component.scss'],
})
export class DiffDashboardComponent implements OnInit {
  product$: Observable<BoatProduct>;
  _product!: BoatProduct;

  releases$: Observable<BoatProductRelease[]>;
  _releaseSpec!: ReleaseSpec;

  release1Control = new FormControl();
  release2Control = new FormControl();
  releaseSpecControl = new FormControl();

  isLoading: boolean = false;
  isLoading$ = new BehaviorSubject(this.isLoading);
  state = ChangeState;

  capabilities: ReleaseCapability[] = [];

  releaseSpec = new ReplaySubject<ReleaseSpec>(1);
  releaseSpec$: Observable<ReleaseSpec> = this.releaseSpec.asObservable();


  changeLogReport: Observable<string>;
  onlyShowChanges: boolean = false;
  expandAll: boolean = false;

  constructor(protected activatedRoute: ActivatedRoute,
              protected dashboardService: DashboardHttpService,
              public matDialog: MatDialog,
              public router: Router) {
    this.product$ = activatedRoute.data.pipe(
      map(({product}) => product));

    this.releases$ = this.product$.pipe(
      switchMap((product) => this.dashboardService.getProductReleases(
        {
          portalKey: product.portalKey,
          productKey: product.key
        }, "response")),
      map((response) => response.body ? response.body : []));

    this.changeLogReport = this.releaseSpec$.pipe(
      switchMap((releaseSpec) => {
        if (releaseSpec && releaseSpec.spec1 && releaseSpec.spec2) {
          return this.dashboardService.getDiffReport(
            {
              portalKey:  releaseSpec.product.portalKey,
              productKey: releaseSpec.product.key,
              spec1Id: releaseSpec.spec1?.id,
              spec2Id: releaseSpec.spec2?.id
            })
        } else {
          return of('');
        }
      }));

  }

  ngOnInit(): void {

    this.release1Control.valueChanges.subscribe(() => {
      this.updateRouter();

    });
    this.release2Control.valueChanges.subscribe(() => {
      this.updateRouter();
    });

    // this.releaseSpec$.subscribe(releaseSpec => {
    //   this.updateRouter();
    // })

    this.isLoading$.subscribe(isLoading => this.isLoading = isLoading);


    combineLatest([this.activatedRoute.queryParams, this.releases$, this.product$])
      .subscribe(sources => {
          console.log("parse query params");

          const params: Params = sources[0];
          const releases: BoatProductRelease[] = sources[1];
          const product = sources[2];

          const release1Param = params["release1"];
          const release2Param = params["release2"];
          const releaseSpec = params["releaseSpec"];


          let isReleaseChanged = false;
          if (release1Param) {
            let value = releases.find(release => release.key === release1Param);
            this.release1Control.setValue(value);
          }
          if (release2Param) {
            let value1 = releases.find(release => release.key === release2Param);
            this.release2Control.setValue(value1);
          } else if (!release1Param && !release2Param) {
            this.release1Control.setValue(releases[releases.length == 1 ? 0 : releases.length - 2])
            this.release2Control.setValue(releases[releases.length - 1])
          }
          if (this.release2Control.value && this.release1Control.value) {
            this.loadSpecs(product, this.release1Control.value, this.release2Control.value);
          }


        }
      );
    //   this.product$.pipe(
    //     switchMap((product) => this.dashboardService.getProductReleases(product.portalKey, product.key)),
    //     map((response) => response.body ? response.body : []),
    //     tap((releases) => {
    //
    //       this.release1Control.setValue(releases[0]);
    //
    //       if (releases.length == 1) {
    //         this.release2Control.setValue(releases[0]);
    //       } else {
    //         this.release2Control.setValue(releases[1]);
    //       }
    //
    //     })).subscribe();
  }

  updateRouter(): void {
    const release1: BoatProductRelease = this.release1Control.value;
    const release2: BoatProductRelease = this.release2Control.value;
    this.router.navigate([], {
        queryParams: {
          releaseSpec: this._releaseSpec?.key(),
          release1: release1?.key,
          release2: release2?.key
        },
        queryParamsHandling: "merge"
      }
    );

  }


  loadSpecs(product: BoatProduct, release1: BoatProductRelease | null, release2: BoatProductRelease | null): void {
    if (!this.isLoading && release1 && release2) {
      this.isLoading$.next(true);

      this.capabilities = [];

      const specs1$: Observable<BoatSpec[]> = this.dashboardService.getProductReleaseSpecs({
        productKey: product.key,
        portalKey: product.portalKey,
        releaseKey: release1.key
      });
      const specs2$: Observable<BoatSpec[]> = this.dashboardService.getProductReleaseSpecs({
        productKey: product.key,
        portalKey: product.portalKey,
        releaseKey: release2.key
      });

      zip(specs1$, specs2$).subscribe(specsResults => {
        const specs1 = specsResults[0];
        const specs2 = specsResults[1];

        this.processSpecs(product, specs1, specs2);
      });
    }

  }

  private processSpecs(product: BoatProduct, specs1: BoatSpec[], specs2: BoatSpec[]) {
    this.capabilities = [];
    for (const spec of specs1) {
      const capability = this.getReleaseCapability(product, spec, false);
      capability.addSpec(spec);
    }

    for (const spec of specs2) {
      const capability = this.getReleaseCapability(product, spec, true);
      capability.compareSpec(spec);
    }

    this.capabilities = this.capabilities.sort((a, b) => {
      if (a.capability.name > b.capability.name) {
        return 1;
      }
      if (a.capability.name < b.capability.name) {
        return -1;
      }
      return 0;

    });

    let releaseCapabilities = this.capabilities.filter(capability => capability.hasChanges());
    if (releaseCapabilities && this.capabilities.length < 0) {
      let releaseCapability = releaseCapabilities[0];
      let releaseSpecs = releaseCapability.specs.filter(spec => spec.state() == ChangeState.CHANGED);
      if (releaseSpecs) {
        this.releaseSpec.next(releaseSpecs[0]);
      }
    }

    this.isLoading$.next(false);
  }

  getReleaseCapability(product: BoatProduct, spec: BoatSpec, isNew: boolean): ReleaseCapability {
    let find = this.capabilities.find(value => value.capability.id === spec.capability.id);
    if (find) {
      return find;
    } else {
      const capability = new ReleaseCapability(product, spec.capability, isNew);
      this.capabilities.push(capability);
      return capability;
    }
  }

  showDiff(product: BoatProduct, releaseSpec: ReleaseSpec) {

    this.matDialog.open(SpecDiffDialogComponent, {
      data: {
        product: product,
        releaseSpec: releaseSpec
      }

    })

  }

  compareRelease(o1: BoatProductRelease, o2: BoatProductRelease) {
    return o1 != null && o2 != null && o1.key === o2.key;
  }

}
