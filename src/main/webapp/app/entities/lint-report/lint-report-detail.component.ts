import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILintReport } from 'app/shared/model/lint-report.model';

@Component({
  selector: 'jhi-lint-report-detail',
  templateUrl: './lint-report-detail.component.html',
})
export class LintReportDetailComponent implements OnInit {
  lintReport: ILintReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintReport }) => (this.lintReport = lintReport));
  }

  previousState(): void {
    window.history.back();
  }
}
