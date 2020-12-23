import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILintReport, LintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from './lint-report.service';
import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from 'app/entities/lint-rule-violation/lint-rule-violation.service';

@Component({
  selector: 'jhi-lint-report-update',
  templateUrl: './lint-report-update.component.html',
})
export class LintReportUpdateComponent implements OnInit {
  isSaving = false;
  lintruleviolations: ILintRuleViolation[] = [];

  editForm = this.fb.group({
    id: [],
    grade: [],
    passed: [],
    lintRuleViolation: [],
  });

  constructor(
    protected lintReportService: LintReportService,
    protected lintRuleViolationService: LintRuleViolationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintReport }) => {
      this.updateForm(lintReport);

      this.lintRuleViolationService
        .query()
        .subscribe((res: HttpResponse<ILintRuleViolation[]>) => (this.lintruleviolations = res.body || []));
    });
  }

  updateForm(lintReport: ILintReport): void {
    this.editForm.patchValue({
      id: lintReport.id,
      grade: lintReport.grade,
      passed: lintReport.passed,
      lintRuleViolation: lintReport.lintRuleViolation,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lintReport = this.createFromForm();
    if (lintReport.id !== undefined) {
      this.subscribeToSaveResponse(this.lintReportService.update(lintReport));
    } else {
      this.subscribeToSaveResponse(this.lintReportService.create(lintReport));
    }
  }

  private createFromForm(): ILintReport {
    return {
      ...new LintReport(),
      id: this.editForm.get(['id'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      passed: this.editForm.get(['passed'])!.value,
      lintRuleViolation: this.editForm.get(['lintRuleViolation'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintReport>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILintRuleViolation): any {
    return item.id;
  }
}
