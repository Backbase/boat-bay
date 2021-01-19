import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { SpecsDataSource } from './specs-data-source';
import { tap } from "rxjs/operators";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatCapability, BoatProduct, BoatSpec } from "../../models/";
import { merge } from "rxjs";

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

  private _product!: BoatProduct;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = [ 'name', 'title','version','grade', 'changed', 'backwardsCompatible', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];

  constructor(private boatDashboardService: BoatDashboardService) {
  }

  ngOnInit() {
    this.dataSource = new SpecsDataSource(this.boatDashboardService);
    this.dataSource.loadServicesForProduct(this._product.portalKey, this._product.key, 'name', 'asc', 0, 25);

  }

  ngAfterViewInit() {
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
