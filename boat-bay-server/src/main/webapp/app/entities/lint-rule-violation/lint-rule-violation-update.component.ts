import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ILintRuleViolation, LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from './lint-rule-violation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ILintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from 'app/entities/lint-report/lint-report.service';

type SelectableEntity = ILintRule | ILintReport;

@Component({
  selector: 'jhi-lint-rule-violation-update',
  templateUrl: './lint-rule-violation-update.component.html',
})
export class LintRuleViolationUpdateComponent implements OnInit {
  isSaving = false;
  lintrules: ILintRule[] = [];
  lintreports: ILintReport[] = [];

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
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected lintRuleViolationService: LintRuleViolationService,
    protected lintRuleService: LintRuleService,
    protected lintReportService: LintReportService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => {
      this.updateForm(lintRuleViolation);

      this.lintRuleService.query().subscribe((res: HttpResponse<ILintRule[]>) => (this.lintrules = res.body || []));

      this.lintReportService.query().subscribe((res: HttpResponse<ILintReport[]>) => (this.lintreports = res.body || []));
    });
  }

  updateForm(lintRuleViolation: ILintRuleViolation): void {
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
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('boatBayApp.error', { message: err.message })
      );
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

  private createFromForm(): ILintRuleViolation {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintRuleViolation>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
