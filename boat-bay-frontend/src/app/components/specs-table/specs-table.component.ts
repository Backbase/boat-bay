import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable} from '@angular/material/table';
import {SpecsDataSource} from './specs-data-source';
import {map, switchMap, tap} from "rxjs/operators";
import {combineLatest, merge, Observable} from "rxjs";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {FormControl} from "@angular/forms";
import {BoatCapability} from "../../services/dashboard/model/boatCapability";
import {BoatProductRelease} from "../../services/dashboard/model/boatProductRelease";
import {BoatService} from "../../services/dashboard/model/boatService";
import {Changes} from "../../services/dashboard/model/changes";
import {BoatSpec} from "../../services/dashboard/model/boatSpec";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {DashboardHttpService, GetPortalCapabilitiesRequestParams} from "../../services/dashboard/api/dashboard.service";
import {throwNoPortalAttachedError} from "@angular/cdk/portal/portal-errors";

export interface SpecFilter {
  portalKey: string,
  productKey: string,
  capabilities?: BoatCapability[]
  services?: BoatService[]
  release?: BoatProductRelease
  changes?: Changes
}

// noinspection DuplicatedCode
@Component({
  selector: 'specs-table',
  templateUrl: 'specs-table.component.html',
  styleUrls: ['specs-table.component.scss']
})
export class SpecsTableComponent implements AfterViewInit, OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<BoatSpec>;
  dataSource!: SpecsDataSource;

  public capabilities$: Observable<BoatCapability[]>;
  public services$: Observable<BoatService[]>;
  public product$: Observable<BoatProduct>;
  public releases$: Observable<BoatProductRelease[]>;
  public pageSize: number = 25;

  private _specFilter!: SpecFilter;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'title', 'version', 'capability', 'serviceDefinition', 'grade', 'changes', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];
  selectedCapabilities = new FormControl();
  selectedServices = new FormControl()
  selectedRelease = new FormControl();


  constructor(private boatDashboardService: DashboardHttpService,
              public activatedRoute: ActivatedRoute,
              public routerService: Router) {

    this.product$ = activatedRoute.data.pipe(
      map(({product}) => product));
    this.capabilities$ = this.product$.pipe(
      switchMap(product => {
        return this.boatDashboardService.getPortalCapabilities(
          {
            portalKey: product.portalKey,
            productKey: product.key,
            size: 100,
            page: 0,
            sort: [
              "name,asc"
            ]
          }, 'response').pipe(
          map(response => response.body ? response.body : []));
      }));
    this.services$ = this.product$.pipe(
      switchMap(product => this.boatDashboardService.getPortalServices(
        {
          portalKey: product.portalKey,
          productKey: product.key,
          size: 100,
          page: 0,
          sort: [
            "name,asc"
          ]
        }, 'response').pipe(
        map(response => response.body ? response.body : []))));
    this.releases$ = this.product$.pipe(
      switchMap(product => this.boatDashboardService.getProductReleases({
        portalKey: product.portalKey,
        productKey: product.key
      }, 'response').pipe(
        map(response => response.body ? response.body : []))));
    this.product$.subscribe(product => {
      this._specFilter = {
        portalKey: product.portalKey,
        productKey: product.key
      }
    })
  }

  ngOnInit() {
    this.dataSource = new SpecsDataSource(this.boatDashboardService);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadData())
      )
      .subscribe();

    combineLatest([this.activatedRoute.queryParams, this.capabilities$, this.services$, this.releases$]).subscribe(value => {
      const params: Params = value[0];
      const capabilities: BoatCapability[] = value[1];
      const services: BoatService[] = value[2];
      const releases: BoatProductRelease[] = value[3];
      let capabilityParam = params["capability"];
      let serviceParam = params["service"];
      let releaseParam = params["release"]

      if (capabilityParam) {
        const keys: string[] = Array.isArray(capabilityParam) ? capabilityParam : [capabilityParam];
        const selectedCapabilities = capabilities.filter(item => keys.some(key => item.key === key));
        if (selectedCapabilities) {
          this.selectedCapabilities.setValue(selectedCapabilities)
          this._specFilter.capabilities = selectedCapabilities;
        }
      }
      if (serviceParam) {
        const keys: string[] = Array.isArray(serviceParam) ? serviceParam : [serviceParam];
        const selectedServices = services.filter(item => keys.some(key => item.key === key));
        if (selectedServices) {
          this.selectedServices.setValue(selectedServices)
          this._specFilter.services = selectedServices;
        }
      }
      if (releaseParam) {
        const keys: string[] = Array.isArray(releaseParam) ? releaseParam : [releaseParam];
        const release = releases.find(item => keys.some(key => item.key === key));
        if (release) {
          this.selectedRelease.setValue(release)
          this._specFilter.release = release;
        }
      }
      console.log("Loading Specs with filter: ", this._specFilter);
      this.dataSource.loadSpecs(this._specFilter, 'name', 'asc', 0, this.pageSize);
    });

    this.selectedCapabilities.valueChanges.subscribe(source => this.updateRouter());
    this.selectedServices.valueChanges.subscribe(source => this.updateRouter());
    this.selectedRelease.valueChanges.subscribe(source => this.updateRouter());
  }

  updateRouter(): void {
    console.log("Update Router");
    this.routerService.navigate([], {
      queryParams: {
        capability: this.selectedCapabilities.value?.map((item: BoatCapability) => item.key),
        service: this.selectedServices.value?.map((item: BoatService) => item.key),
        release: this.selectedRelease.value?.key
      },
      queryParamsHandling: "merge"
    })

    this._specFilter.capabilities = this.selectedCapabilities.value;
    this._specFilter.services = this.selectedServices.value;
    this._specFilter.release = this.selectedRelease.value;
  }

  loadData(): void {
    this.dataSource.loadSpecs(this._specFilter, this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }

  resetFilter() {
    this.selectedCapabilities.reset();
    this.selectedServices.reset();
    this.selectedRelease.reset();

  }

  compareCap(o1: BoatCapability, o2: BoatCapability) {
    return o1 != null && o2 != null && o1.key === o2.key;
  }

  compareService(o1: BoatService, o2: BoatService) {
    return o1 != null && o2 != null && o1.key === o2.key;
  }

  inSelectedCapability(service: BoatService): boolean {
    if (!this.selectedCapabilities.value || this.selectedCapabilities.value.length === 0) {
      return true;
    }

    const selectedCapabilities: BoatCapability[] = this.selectedCapabilities.value;

    return selectedCapabilities.some((item) => item.key === service.capability.key);
  }
}
