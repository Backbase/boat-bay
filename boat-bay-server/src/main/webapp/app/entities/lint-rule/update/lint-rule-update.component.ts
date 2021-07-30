import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILintRule, LintRule } from '../lint-rule.model';
import { LintRuleService } from '../service/lint-rule.service';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';

@Component({
  selector: 'jhi-lint-rule-update',
  templateUrl: './lint-rule-update.component.html',
})
export class LintRuleUpdateComponent implements OnInit {
  isSaving = false;

  portalsSharedCollection: IPortal[] = [];

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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lintRule }) => {
      this.updateForm(lintRule);

      this.loadRelationshipsOptions();
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

  trackPortalById(index: number, item: IPortal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILintRule>>): void {
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

  protected updateForm(lintRule: ILintRule): void {
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

    this.portalsSharedCollection = this.portalService.addPortalToCollectionIfMissing(this.portalsSharedCollection, lintRule.portal);
  }

  protected loadRelationshipsOptions(): void {
    this.portalService
      .query()
      .pipe(map((res: HttpResponse<IPortal[]>) => res.body ?? []))
      .pipe(map((portals: IPortal[]) => this.portalService.addPortalToCollectionIfMissing(portals, this.editForm.get('portal')!.value)))
      .subscribe((portals: IPortal[]) => (this.portalsSharedCollection = portals));
  }

  protected createFromForm(): ILintRule {
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
}
