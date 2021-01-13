import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPortalLintRule, PortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from './portal-lint-rule.service';
import { ILintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

type SelectableEntity = ILintRule | IPortal;

@Component({
  selector: 'jhi-portal-lint-rule-update',
  templateUrl: './portal-lint-rule-update.component.html',
})
export class PortalLintRuleUpdateComponent implements OnInit {
  isSaving = false;
  lintrules: ILintRule[] = [];
  portals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    ruleId: [null, [Validators.required]],
    enabled: [null, [Validators.required]],
    lintRule: [null, Validators.required],
    portal: [null, Validators.required],
  });

  constructor(
    protected portalLintRuleService: PortalLintRuleService,
    protected lintRuleService: LintRuleService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRule }) => {
      this.updateForm(portalLintRule);

      this.lintRuleService.query().subscribe((res: HttpResponse<ILintRule[]>) => (this.lintrules = res.body || []));

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));
    });
  }

  updateForm(portalLintRule: IPortalLintRule): void {
    this.editForm.patchValue({
      id: portalLintRule.id,
      ruleId: portalLintRule.ruleId,
      enabled: portalLintRule.enabled,
      lintRule: portalLintRule.lintRule,
      portal: portalLintRule.portal,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const portalLintRule = this.createFromForm();
    if (portalLintRule.id !== undefined) {
      this.subscribeToSaveResponse(this.portalLintRuleService.update(portalLintRule));
    } else {
      this.subscribeToSaveResponse(this.portalLintRuleService.create(portalLintRule));
    }
  }

  private createFromForm(): IPortalLintRule {
    return {
      ...new PortalLintRule(),
      id: this.editForm.get(['id'])!.value,
      ruleId: this.editForm.get(['ruleId'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      lintRule: this.editForm.get(['lintRule'])!.value,
      portal: this.editForm.get(['portal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortalLintRule>>): void {
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
