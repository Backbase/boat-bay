import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPortalLintRule, PortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from './portal-lint-rule.service';
import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';
import { PortalLintRuleSetService } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set.service';

@Component({
  selector: 'jhi-portal-lint-rule-update',
  templateUrl: './portal-lint-rule-update.component.html',
})
export class PortalLintRuleUpdateComponent implements OnInit {
  isSaving = false;
  portallintrulesets: IPortalLintRuleSet[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    portalRuleSet: [null, Validators.required],
  });

  constructor(
    protected portalLintRuleService: PortalLintRuleService,
    protected portalLintRuleSetService: PortalLintRuleSetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRule }) => {
      this.updateForm(portalLintRule);

      this.portalLintRuleSetService
        .query()
        .subscribe((res: HttpResponse<IPortalLintRuleSet[]>) => (this.portallintrulesets = res.body || []));
    });
  }

  updateForm(portalLintRule: IPortalLintRule): void {
    this.editForm.patchValue({
      id: portalLintRule.id,
      name: portalLintRule.name,
      portalRuleSet: portalLintRule.portalRuleSet,
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
      name: this.editForm.get(['name'])!.value,
      portalRuleSet: this.editForm.get(['portalRuleSet'])!.value,
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

  trackById(index: number, item: IPortalLintRuleSet): any {
    return item.id;
  }
}
