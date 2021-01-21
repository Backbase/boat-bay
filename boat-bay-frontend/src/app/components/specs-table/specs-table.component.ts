import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { SpecsDataSource } from './specs-data-source';
import { map, switchMap, tap } from "rxjs/operators";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatCapability, BoatProduct, BoatService, BoatSpec } from "../../models/";
import { merge, Observable } from "rxjs";
import { BoatProductRelease } from "../../models/boat-product-release";

export interface SpecFilter {
  capabilities?: BoatCapability[]
  services?: BoatService[]
  releases?: BoatProductRelease[]
  backwardsCompatible?: boolean
}

@Component({
  selector: 'specs-table',
  templateUrl: 'specs-table.component.html',
  styleUrls: ['specs-table.component.scss']
})
export class SpecsTableComponent implements AfterViewInit, OnInit {

  @Input()
  set product(product: BoatProduct) {
    this._product = product;
  }

  get product():BoatProduct {
    return this._product;
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<BoatSpec>;
  dataSource!: SpecsDataSource;

  public capabilities$: Observable<BoatCapability[]> | null = null
  public services$: Observable<BoatService[]> | null = null

  private _product!: BoatProduct;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = [ 'title','version', 'capability', 'serviceDefinition','grade', 'changed', 'backwardsCompatible', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];
  selectedCapabilities: any;

  constructor(private boatDashboardService: BoatDashboardService) {

  }

  ngOnInit() {
    this.dataSource = new SpecsDataSource(this.boatDashboardService);
    this.capabilities$ = this.boatDashboardService.getBoatCapabilities(this._product.portalKey, this._product.key, 0, 100, 'name', 'asc').pipe(map(response => response.body ? response.body : []));
    this.services$ = this.boatDashboardService.getBoatServices(this._product.portalKey, this._product.key, 0, 100, 'name', 'asc').pipe(map(response => response.body ? response.body : []));
  }

  ngAfterViewInit() {
    this.loadData();
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadData())
      )
      .subscribe();
  }

  loadData(): void {
    this.dataSource.loadServicesForProduct(this._product.portalKey, this._product.key, this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
