import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

@Component({
  selector: 'jhi-lint-rule-violation-detail',
  templateUrl: './lint-rule-violation-detail.component.html',
})
export class LintRuleViolationDetailComponent implements OnInit {
  lintRuleViolation: ILintRuleViolation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => (this.lintRuleViolation = lintRuleViolation));
  }

  previousState(): void {
    window.history.back();
  }
}
