import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ILintRuleViolation, LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from './lint-rule-violation.service';
import { ILintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';

@Component({
  selector: 'jhi-lint-rule-violation-update',
  templateUrl: './lint-rule-violation-update.component.html',
})
export class LintRuleViolationUpdateComponent implements OnInit {
  isSaving = false;
  lintrules: ILintRule[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    severity: [],
    lineStart: [],
    lindEnd: [],
    columnStart: [],
    columnEnd: [],
    jsonPointer: [],
    lintRule: [],
  });

  constructor(
    protected lintRuleViolationService: LintRuleViolationService,
    protected lintRuleService: LintRuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleViolation }) => {
      this.updateForm(lintRuleViolation);

      this.lintRuleService
        .query({ filter: 'lintruleviolation-is-null' })
        .pipe(
          map((res: HttpResponse<ILintRule[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ILintRule[]) => {
          if (!lintRuleViolation.lintRule || !lintRuleViolation.lintRule.id) {
            this.lintrules = resBody;
          } else {
            this.lintRuleService
              .find(lintRuleViolation.lintRule.id)
              .pipe(
                map((subRes: HttpResponse<ILintRule>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILintRule[]) => (this.lintrules = concatRes));
          }
        });
    });
  }

  updateForm(lintRuleViolation: ILintRuleViolation): void {
    this.editForm.patchValue({
      id: lintRuleViolation.id,
      name: lintRuleViolation.name,
      description: lintRuleViolation.description,
      severity: lintRuleViolation.severity,
      lineStart: lintRuleViolation.lineStart,
      lindEnd: lintRuleViolation.lindEnd,
      columnStart: lintRuleViolation.columnStart,
      columnEnd: lintRuleViolation.columnEnd,
      jsonPointer: lintRuleViolation.jsonPointer,
      lintRule: lintRuleViolation.lintRule,
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
      severity: this.editForm.get(['severity'])!.value,
      lineStart: this.editForm.get(['lineStart'])!.value,
      lindEnd: this.editForm.get(['lindEnd'])!.value,
      columnStart: this.editForm.get(['columnStart'])!.value,
      columnEnd: this.editForm.get(['columnEnd'])!.value,
      jsonPointer: this.editForm.get(['jsonPointer'])!.value,
      lintRule: this.editForm.get(['lintRule'])!.value,
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

  trackById(index: number, item: ILintRule): any {
    return item.id;
  }
}
