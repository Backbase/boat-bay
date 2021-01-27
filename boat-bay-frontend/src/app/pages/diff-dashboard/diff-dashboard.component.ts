import { Component, OnInit } from '@angular/core';
import { Observable, of, ReplaySubject, zip } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { BoatCapability, BoatProduct, BoatSpec } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatProductRelease } from "../../models/boat-product-release";
import { FormControl } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { SpecDiffDialogComponent } from "../../components/spec-diff-dialog/spec-diff-dialog.component";

export enum ChangeState {
  NEW, DELETED, CHANGED, UNCHANGED
}

export class ReleaseSpec {

  constructor(public product: BoatProduct, public spec1: BoatSpec | null, public spec2: BoatSpec | null = null) {
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
  releases1: BoatProductRelease[] = [];
  releases2: BoatProductRelease[] = [];

  release1Control = new FormControl();
  release2Control = new FormControl();

  isLoading: boolean = false;
  state = ChangeState;

  capabilities: ReleaseCapability[] = [];

  releaseSpec = new ReplaySubject<ReleaseSpec>(1);
  releaseSpec$: Observable<ReleaseSpec> = this.releaseSpec.asObservable();

  changeLogReport: Observable<string>;
  onlyShowChanges: boolean = false;

  constructor(protected activatedRoute: ActivatedRoute,
              protected dashboardService: BoatDashboardService,
              public matDialog: MatDialog) {
    this.product$ = activatedRoute.data.pipe(
      map(({product}) => product),
      tap(product => {

          this.release1Control.valueChanges.subscribe(() => {
            this.loadSpecs(product, this.release1Control.value, this.release2Control.value);
          });
          this.release2Control.valueChanges.subscribe(() => {
            this.loadSpecs(product, this.release1Control.value, this.release2Control.value);
          });
        }
      ));


    this.changeLogReport = this.releaseSpec$.pipe(
      switchMap((releaseSpec) => {
        if (releaseSpec && releaseSpec.spec1 && releaseSpec.spec2) {
          return this.dashboardService.getSpecDiffReportAsHtml(
            releaseSpec.product.portalKey,
            releaseSpec.product.key, releaseSpec.spec1.id, releaseSpec.spec2.id)
        } else {
          return of('');
        }
      }));
  }

  ngOnInit(): void {
    this.product$.pipe(
      switchMap((product) => this.dashboardService.getProductReleases(product.portalKey, product.key)),
      map((response) => response.body ? response.body : []),
      tap((releases) => {
        this.releases1 = releases;
        this.releases2 = releases;
        this.release1Control.setValue(releases[0]);

        if(releases.length==1) {
          this.release2Control.setValue(releases[0]);
        } else {
          this.release2Control.setValue(releases[1]);
        }



      })).subscribe();
  }

  loadSpecs(product: BoatProduct, release1: BoatProductRelease | null, release2: BoatProductRelease | null): void {
    if (!this.isLoading && release1 && release2) {
      this.isLoading = true;
      this.capabilities = [];

      const specs1$: Observable<BoatSpec[]> = this.dashboardService.getProductReleaseSpecs(product.portalKey, product.key, release1.key)
        .pipe(map(({body}) => body ? body : []));
      const specs2$: Observable<BoatSpec[]> = this.dashboardService.getProductReleaseSpecs(product.portalKey, product.key, release2.key)
        .pipe(map(({body}) => body ? body : []));


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

    this.isLoading = false;
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

  showDiff(product:BoatProduct,  releaseSpec: ReleaseSpec) {

    this.matDialog.open(SpecDiffDialogComponent, {
      data: {
        product: product,
        releaseSpec: releaseSpec
      }

    })

  }
}
