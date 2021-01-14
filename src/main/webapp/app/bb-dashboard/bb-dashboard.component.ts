import { Component, OnInit } from '@angular/core';
import { BoatDashboardService } from 'app/services/boat-dashboard.service';
import { Observable } from 'rxjs';
import { BoatDashboard } from 'app/models/dashboard/boat-dashboard';
import { map } from 'rxjs/operators';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { ILintRule } from 'app/shared/model/lint-rule.model';
import { BoatLintReportService } from 'app/services/boat-lint-report.service';

@Component({
  selector: 'bb-dashboard',
  templateUrl: './bb-dashboard.component.html',
  styleUrls: ['./bb-dashboard.component.scss'],
})
export class BbDashboardComponent implements OnInit {
  boatPortals$: Observable<BoatDashboard[]>;
  lintRules$: Observable<ILintRule[] | null>;

  constructor(
    protected boatDashboardService: BoatDashboardService,
    protected boatLintReportService: BoatLintReportService,
    protected lintRuleService: LintRuleService
  ) {
    this.boatPortals$ = boatDashboardService.getBoatPortalView().pipe(map(value => value));
    this.lintRules$ = lintRuleService.query().pipe(map(value => value.body));
  }

  ngOnInit(): void {}

  toggleRule(rule: ILintRule): void {
    this.lintRuleService.update(rule).subscribe();
  }

  analyse(portal: BoatDashboard): void {
    this.boatLintReportService.postLintProduct(portal.productId).subscribe();
  }
}
