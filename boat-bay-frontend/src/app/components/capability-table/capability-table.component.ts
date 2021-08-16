import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { CapabilityDatasource } from './capability-datasource';
import { tap } from "rxjs/operators";
import { merge } from "rxjs";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {BoatCapability} from "../../services/dashboard/model/boatCapability";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";

@Component({
  selector: 'capability-table',
  templateUrl: 'capability-table.component.html',
  styleUrls: ['capability-table.component.scss']
})
export class CapabilityTableComponent implements AfterViewInit, OnInit {

  @Input()
  set product(product: BoatProduct) {
    this._product = product;
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<BoatCapability>;
  dataSource!: CapabilityDatasource;

  private _product!: BoatProduct;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];

  constructor(private boatDashboardService: DashboardHttpService) {
  }

  ngOnInit() {
    this.dataSource = new CapabilityDatasource(this.boatDashboardService);
    this.dataSource.loadCapabilitiesForProduct(this._product.portalKey, this._product.key, 'name', 'asc', 0, 10);

  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadData())
      )
      .subscribe();
  }

  loadData(): void {
    this.dataSource.loadCapabilitiesForProduct(this._product.portalKey, this._product.key, this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
