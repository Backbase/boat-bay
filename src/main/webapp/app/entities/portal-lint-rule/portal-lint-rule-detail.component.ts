import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';

@Component({
  selector: 'jhi-portal-lint-rule-detail',
  templateUrl: './portal-lint-rule-detail.component.html',
})
export class PortalLintRuleDetailComponent implements OnInit {
  portalLintRule: IPortalLintRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRule }) => (this.portalLintRule = portalLintRule));
  }

  previousState(): void {
    window.history.back();
  }
}
