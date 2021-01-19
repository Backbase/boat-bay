import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILintRule } from 'app/shared/model/lint-rule.model';

@Component({
  selector: 'jhi-lint-rule-detail',
  templateUrl: './lint-rule-detail.component.html',
})
export class LintRuleDetailComponent implements OnInit {
  lintRule: ILintRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRule }) => (this.lintRule = lintRule));
  }

  previousState(): void {
    window.history.back();
  }
}
