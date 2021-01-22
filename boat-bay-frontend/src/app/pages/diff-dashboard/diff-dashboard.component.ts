import { Component, OnInit } from '@angular/core';
import { Observable, pipe } from 'rxjs';
import { filter, map, switchMap, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { BoatProduct } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatProductRelease } from "../../models/boat-product-release";

@Component({
  selector: 'bb-product-dashboard',
  templateUrl: 'diff-dashboard.component.html',
  styleUrls: ['diff-dashboard.component.scss'],
})
export class DiffDashboardComponent implements OnInit {
  product$: Observable<BoatProduct>;
  releases1: BoatProductRelease[] = [];
  releases2: BoatProductRelease[] = [];

  constructor(protected activatedRoute: ActivatedRoute, protected boatLintReportService: BoatDashboardService) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));

    this.product$.pipe(
      switchMap((product) => this.boatLintReportService.getReleases(product.portalKey, product.key)),
      map((response) => response.body ? response.body : []),
      tap((releases) => {
        this.releases1 = releases;
        this.releases2 = releases;
      })).subscribe();
  }

  ngOnInit(): void {
  }

  // toggleRule(rule: ILintRule): void {
  //   this.lintRuleService.update(rule).subscribe();
  // }
  //
  // analyse(portal: BoatDashboard): void {
  //   this.boatLintReportService.postLintProduct(portal.productId).subscribe();
  // }
  // analyse(capability: BoatCapability): void {
  //   this.boatLintReportService.postLintCapability(capability.id).subscribe();
  // }
}
