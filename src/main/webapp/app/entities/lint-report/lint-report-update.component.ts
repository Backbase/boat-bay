import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ILintReport, LintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from './lint-report.service';

@Component({
  selector: 'jhi-lint-report-update',
  templateUrl: './lint-report-update.component.html',
})
export class LintReportUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    grade: [],
    passed: [],
    lintedOn: [],
  });

  constructor(protected lintReportService: LintReportService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintReport }) => {
      if (!lintReport.id) {
        const today = moment().startOf('day');
        lintReport.lintedOn = today;
      }

      this.updateForm(lintReport);
    });
  }

  updateForm(lintReport: ILintReport): void {
    this.editForm.patchValue({
      id: lintReport.id,
      name: lintReport.name,
      grade: lintReport.grade,
      passed: lintReport.passed,
      lintedOn: lintReport.lintedOn ? lintReport.lintedOn.format(DATE_TIME_FORMAT) : null,
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
      name: this.editForm.get(['name'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      passed: this.editForm.get(['passed'])!.value,
      lintedOn: this.editForm.get(['lintedOn'])!.value ? moment(this.editForm.get(['lintedOn'])!.value, DATE_TIME_FORMAT) : undefined,
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
}
