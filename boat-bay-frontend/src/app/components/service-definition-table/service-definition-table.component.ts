import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { ServiceDefinitionDatasource } from './service-definition-datasource';
import { tap } from "rxjs/operators";
import { BoatCapability, BoatProduct } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { merge } from "rxjs";

@Component({
  selector: 'service-definition-table',
  templateUrl: 'service-definition-table.component.html',
  styleUrls: ['service-definition-table.component.scss']
})
export class ServiceDefinitionTableComponent implements AfterViewInit, OnInit {

  @Input()
  set product(product: BoatProduct) {
    this._product = product;
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<BoatCapability>;
  dataSource!: ServiceDefinitionDatasource;

  private _product!: BoatProduct;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];

  constructor(private boatDashboardService: BoatDashboardService) {
  }

  ngOnInit() {
    this.dataSource = new ServiceDefinitionDatasource(this.boatDashboardService);
    this.dataSource.loadServicesForProduct(this._product.portalKey, this._product.key, 'name', 'asc', 0, 10);

  }

  ngAfterViewInit() {
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
