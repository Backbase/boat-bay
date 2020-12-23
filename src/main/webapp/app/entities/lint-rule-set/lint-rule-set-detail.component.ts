import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';

@Component({
  selector: 'jhi-lint-rule-set-detail',
  templateUrl: './lint-rule-set-detail.component.html',
})
export class LintRuleSetDetailComponent implements OnInit {
  lintRuleSet: ILintRuleSet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleSet }) => (this.lintRuleSet = lintRuleSet));
  }

  previousState(): void {
    window.history.back();
  }
}
