import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILintReport, LintReport } from '../lint-report.model';
import { LintReportService } from '../service/lint-report.service';
import { ISpec } from 'app/entities/spec/spec.model';
import { SpecService } from 'app/entities/spec/service/spec.service';

@Component({
  selector: 'jhi-lint-report-update',
  templateUrl: './lint-report-update.component.html',
})
export class LintReportUpdateComponent implements OnInit {
  isSaving = false;

  specsCollection: ISpec[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    grade: [],
    passed: [],
    lintedOn: [],
    spec: [],
  });

  constructor(
    protected lintReportService: LintReportService,
    protected specService: SpecService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintReport }) => {
      if (lintReport.id === undefined) {
        const today = dayjs().startOf('day');
        lintReport.lintedOn = today;
      }

      this.updateForm(lintReport);

      this.loadRelationshipsOptions();
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

  trackSpecById(index: number, item: ISpec): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintReport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(lintReport: ILintReport): void {
    this.editForm.patchValue({
      id: lintReport.id,
      name: lintReport.name,
      grade: lintReport.grade,
      passed: lintReport.passed,
      lintedOn: lintReport.lintedOn ? lintReport.lintedOn.format(DATE_TIME_FORMAT) : null,
      spec: lintReport.spec,
    });

    this.specsCollection = this.specService.addSpecToCollectionIfMissing(this.specsCollection, lintReport.spec);
  }

  protected loadRelationshipsOptions(): void {
    this.specService
      .query({ filter: 'lintreport-is-null' })
      .pipe(map((res: HttpResponse<ISpec[]>) => res.body ?? []))
      .pipe(map((specs: ISpec[]) => this.specService.addSpecToCollectionIfMissing(specs, this.editForm.get('spec')!.value)))
      .subscribe((specs: ISpec[]) => (this.specsCollection = specs));
  }

  protected createFromForm(): ILintReport {
    return {
      ...new LintReport(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      passed: this.editForm.get(['passed'])!.value,
      lintedOn: this.editForm.get(['lintedOn'])!.value ? dayjs(this.editForm.get(['lintedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      spec: this.editForm.get(['spec'])!.value,
    };
  }
}
