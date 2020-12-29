import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

@Component({
  selector: 'jhi-portal-lint-rule-set-detail',
  templateUrl: './portal-lint-rule-set-detail.component.html',
})
export class PortalLintRuleSetDetailComponent implements OnInit {
  portalLintRuleSet: IPortalLintRuleSet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRuleSet }) => (this.portalLintRuleSet = portalLintRuleSet));
  }

  previousState(): void {
    window.history.back();
  }
}
