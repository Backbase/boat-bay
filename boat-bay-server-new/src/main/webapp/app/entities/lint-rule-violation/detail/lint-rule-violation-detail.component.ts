import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILintRuleViolation } from '../lint-rule-violation.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-lint-rule-violation-detail',
  templateUrl: './lint-rule-violation-detail.component.html',
})
export class LintRuleViolationDetailComponent implements OnInit {
  lintRuleViolation: ILintRuleViolation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => {
      this.lintRuleViolation = lintRuleViolation;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
