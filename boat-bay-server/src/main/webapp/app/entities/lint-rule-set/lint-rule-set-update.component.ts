import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ILintRuleSet, LintRuleSet } from 'app/shared/model/lint-rule-set.model';
import { LintRuleSetService } from './lint-rule-set.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-lint-rule-set-update',
  templateUrl: './lint-rule-set-update.component.html',
})
export class LintRuleSetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ruleSetId: [null, [Validators.required]],
    name: [null, [Validators.required]],
    description: [],
    externalUrl: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected lintRuleSetService: LintRuleSetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRuleSet }) => {
      this.updateForm(lintRuleSet);
    });
  }

  updateForm(lintRuleSet: ILintRuleSet): void {
    this.editForm.patchValue({
      id: lintRuleSet.id,
      ruleSetId: lintRuleSet.ruleSetId,
      name: lintRuleSet.name,
      description: lintRuleSet.description,
      externalUrl: lintRuleSet.externalUrl,
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
    const lintRuleSet = this.createFromForm();
    if (lintRuleSet.id !== undefined) {
      this.subscribeToSaveResponse(this.lintRuleSetService.update(lintRuleSet));
    } else {
      this.subscribeToSaveResponse(this.lintRuleSetService.create(lintRuleSet));
    }
  }

  private createFromForm(): ILintRuleSet {
    return {
      ...new LintRuleSet(),
      id: this.editForm.get(['id'])!.value,
      ruleSetId: this.editForm.get(['ruleSetId'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      externalUrl: this.editForm.get(['externalUrl'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintRuleSet>>): void {
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
