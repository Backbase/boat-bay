import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILintRule, LintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from './lint-rule.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

@Component({
  selector: 'jhi-lint-rule-update',
  templateUrl: './lint-rule-update.component.html',
})
export class LintRuleUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    ruleId: [null, [Validators.required]],
    title: [null, [Validators.required]],
    ruleSet: [],
    summary: [],
    severity: [null, [Validators.required]],
    description: [],
    externalUrl: [],
    enabled: [null, [Validators.required]],
    portal: [null, Validators.required],
  });

  constructor(
    protected lintRuleService: LintRuleService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRule }) => {
      this.updateForm(lintRule);

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));
    });
  }

  updateForm(lintRule: ILintRule): void {
    this.editForm.patchValue({
      id: lintRule.id,
      ruleId: lintRule.ruleId,
      title: lintRule.title,
      ruleSet: lintRule.ruleSet,
      summary: lintRule.summary,
      severity: lintRule.severity,
      description: lintRule.description,
      externalUrl: lintRule.externalUrl,
      enabled: lintRule.enabled,
      portal: lintRule.portal,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lintRule = this.createFromForm();
    if (lintRule.id !== undefined) {
      this.subscribeToSaveResponse(this.lintRuleService.update(lintRule));
    } else {
      this.subscribeToSaveResponse(this.lintRuleService.create(lintRule));
    }
  }

  private createFromForm(): ILintRule {
    return {
      ...new LintRule(),
      id: this.editForm.get(['id'])!.value,
      ruleId: this.editForm.get(['ruleId'])!.value,
      title: this.editForm.get(['title'])!.value,
      ruleSet: this.editForm.get(['ruleSet'])!.value,
      summary: this.editForm.get(['summary'])!.value,
      severity: this.editForm.get(['severity'])!.value,
      description: this.editForm.get(['description'])!.value,
      externalUrl: this.editForm.get(['externalUrl'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      portal: this.editForm.get(['portal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintRule>>): void {
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

  trackById(index: number, item: IPortal): any {
    return item.id;
  }
}
