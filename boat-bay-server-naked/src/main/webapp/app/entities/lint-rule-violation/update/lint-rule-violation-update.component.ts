import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILintRuleViolation, LintRuleViolation } from '../lint-rule-violation.model';
import { LintRuleViolationService } from '../service/lint-rule-violation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ILintRule } from 'app/entities/lint-rule/lint-rule.model';
import { LintRuleService } from 'app/entities/lint-rule/service/lint-rule.service';
import { ILintReport } from 'app/entities/lint-report/lint-report.model';
import { LintReportService } from 'app/entities/lint-report/service/lint-report.service';

@Component({
  selector: 'jhi-lint-rule-violation-update',
  templateUrl: './lint-rule-violation-update.component.html',
})
export class LintRuleViolationUpdateComponent implements OnInit {
  isSaving = false;

  lintRulesSharedCollection: ILintRule[] = [];
  lintReportsSharedCollection: ILintReport[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    url: [],
    severity: [],
    lineStart: [],
    lineEnd: [],
    jsonPointer: [],
    lintRule: [null, Validators.required],
    lintReport: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected lintRuleViolationService: LintRuleViolationService,
    protected lintRuleService: LintRuleService,
    protected lintReportService: LintReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => {
      this.updateForm(lintRuleViolation);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('boatbayApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lintRuleViolation = this.createFromForm();
    if (lintRuleViolation.id !== undefined) {
      this.subscribeToSaveResponse(this.lintRuleViolationService.update(lintRuleViolation));
    } else {
      this.subscribeToSaveResponse(this.lintRuleViolationService.create(lintRuleViolation));
    }
  }

  trackLintRuleById(index: number, item: ILintRule): number {
    return item.id!;
  }

  trackLintReportById(index: number, item: ILintReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintRuleViolation>>): void {
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

  protected updateForm(lintRuleViolation: ILintRuleViolation): void {
    this.editForm.patchValue({
      id: lintRuleViolation.id,
      name: lintRuleViolation.name,
      description: lintRuleViolation.description,
      url: lintRuleViolation.url,
      severity: lintRuleViolation.severity,
      lineStart: lintRuleViolation.lineStart,
      lineEnd: lintRuleViolation.lineEnd,
      jsonPointer: lintRuleViolation.jsonPointer,
      lintRule: lintRuleViolation.lintRule,
      lintReport: lintRuleViolation.lintReport,
    });

    this.lintRulesSharedCollection = this.lintRuleService.addLintRuleToCollectionIfMissing(
      this.lintRulesSharedCollection,
      lintRuleViolation.lintRule
    );
    this.lintReportsSharedCollection = this.lintReportService.addLintReportToCollectionIfMissing(
      this.lintReportsSharedCollection,
      lintRuleViolation.lintReport
    );
  }

  protected loadRelationshipsOptions(): void {
    this.lintRuleService
      .query()
      .pipe(map((res: HttpResponse<ILintRule[]>) => res.body ?? []))
      .pipe(
        map((lintRules: ILintRule[]) =>
          this.lintRuleService.addLintRuleToCollectionIfMissing(lintRules, this.editForm.get('lintRule')!.value)
        )
      )
      .subscribe((lintRules: ILintRule[]) => (this.lintRulesSharedCollection = lintRules));

    this.lintReportService
      .query()
      .pipe(map((res: HttpResponse<ILintReport[]>) => res.body ?? []))
      .pipe(
        map((lintReports: ILintReport[]) =>
          this.lintReportService.addLintReportToCollectionIfMissing(lintReports, this.editForm.get('lintReport')!.value)
        )
      )
      .subscribe((lintReports: ILintReport[]) => (this.lintReportsSharedCollection = lintReports));
  }

  protected createFromForm(): ILintRuleViolation {
    return {
      ...new LintRuleViolation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      url: this.editForm.get(['url'])!.value,
      severity: this.editForm.get(['severity'])!.value,
      lineStart: this.editForm.get(['lineStart'])!.value,
      lineEnd: this.editForm.get(['lineEnd'])!.value,
      jsonPointer: this.editForm.get(['jsonPointer'])!.value,
      lintRule: this.editForm.get(['lintRule'])!.value,
      lintReport: this.editForm.get(['lintReport'])!.value,
    };
  }
}
