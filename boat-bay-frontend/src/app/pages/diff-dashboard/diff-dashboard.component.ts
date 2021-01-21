import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { BoatProduct } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";

@Component({
  selector: 'bb-product-dashboard',
  templateUrl: 'diff-dashboard.component.html',
  styleUrls: ['diff-dashboard.component.scss'],
})
export class DiffDashboardComponent implements OnInit {
  product$: Observable<BoatProduct>;

  constructor(protected activatedRoute: ActivatedRoute, protected boatLintReportService: BoatDashboardService) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));
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
