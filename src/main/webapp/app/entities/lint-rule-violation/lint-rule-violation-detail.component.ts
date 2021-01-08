import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

@Component({
  selector: 'jhi-lint-rule-violation-detail',
  templateUrl: './lint-rule-violation-detail.component.html',
})
export class LintRuleViolationDetailComponent implements OnInit {
  lintRuleViolation: ILintRuleViolation | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => (this.lintRuleViolation = lintRuleViolation));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
