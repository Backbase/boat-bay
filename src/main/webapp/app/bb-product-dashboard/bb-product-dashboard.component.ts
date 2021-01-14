import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { BoatProductDashboard } from 'app/models/dashboard/boat-product-dashboard';
import { BoatCapability } from 'app/models/dashboard/boat-capability';
import { BoatLintReportService } from 'app/services/boat-lint-report.service';

@Component({
  selector: 'bb-product-dashboard',
  templateUrl: './bb-product-dashboard.component.html',
  styleUrls: ['./bb-product-dashboard.component.scss'],
})
export class BbProductDashboardComponent implements OnInit {
  productDashboard$: Observable<BoatProductDashboard>;

  constructor(protected activatedRoute: ActivatedRoute, protected boatLintReportService: BoatLintReportService) {
    this.productDashboard$ = activatedRoute.data.pipe(map(({ productDashboard }) => productDashboard));
  }

  ngOnInit(): void {}

  // toggleRule(rule: ILintRule): void {
  //   this.lintRuleService.update(rule).subscribe();
  // }
  //
  // analyse(portal: BoatDashboard): void {
  //   this.boatLintReportService.postLintProduct(portal.productId).subscribe();
  // }
  analyse(capability: BoatCapability): void {
    this.boatLintReportService.postLintCapability(capability.id).subscribe();
  }
}
