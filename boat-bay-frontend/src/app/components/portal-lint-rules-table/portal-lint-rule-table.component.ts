import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { PortalLintRuleDataSource } from './portal-lint-rule-data-source';
import { tap } from "rxjs/operators";
import { BoatProduct } from "../../models/dashboard/boat-product";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatSpec } from "../../models/dashboard/boat-spec";
import { merge } from "rxjs";

@Component({
  selector: 'specs-table',
  templateUrl: 'portal-lint-rule-table.component.html',
  styleUrls: ['portal-lint-rule-table.component.scss']
})
export class PortalLintRuleTableComponent implements AfterViewInit, OnInit {

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
  dataSource!: PortalLintRuleDataSource;

  private _product!: BoatProduct;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = [ 'name', 'title','version','grade', 'changed', 'backwardsCompatible', 'createdOn', 'createdBy', 'violationsMust', 'violationsShould', 'violationsMay', 'violationsHint'];

  constructor(private boatDashboardService: BoatDashboardService) {
  }

  ngOnInit() {
    this.dataSource = new PortalLintRuleDataSource(this.boatDashboardService);
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
