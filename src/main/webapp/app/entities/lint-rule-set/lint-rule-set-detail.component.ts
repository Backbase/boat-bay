import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';

@Component({
  selector: 'jhi-lint-rule-set-detail',
  templateUrl: './lint-rule-set-detail.component.html',
})
export class LintRuleSetDetailComponent implements OnInit {
  lintRuleSet: ILintRuleSet | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleSet }) => (this.lintRuleSet = lintRuleSet));
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
