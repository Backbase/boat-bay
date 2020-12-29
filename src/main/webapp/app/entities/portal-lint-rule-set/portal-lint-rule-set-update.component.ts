import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPortalLintRuleSet, PortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';
import { PortalLintRuleSetService } from './portal-lint-rule-set.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

@Component({
  selector: 'jhi-portal-lint-rule-set-update',
  templateUrl: './portal-lint-rule-set-update.component.html',
})
export class PortalLintRuleSetUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    portal: [null, Validators.required],
  });

  constructor(
    protected portalLintRuleSetService: PortalLintRuleSetService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRuleSet }) => {
      this.updateForm(portalLintRuleSet);

      this.portalService
        .query({ filter: 'portalruleset-is-null' })
        .pipe(
          map((res: HttpResponse<IPortal[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPortal[]) => {
          if (!portalLintRuleSet.portal || !portalLintRuleSet.portal.id) {
            this.portals = resBody;
          } else {
            this.portalService
              .find(portalLintRuleSet.portal.id)
              .pipe(
                map((subRes: HttpResponse<IPortal>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPortal[]) => (this.portals = concatRes));
          }
        });
    });
  }

  updateForm(portalLintRuleSet: IPortalLintRuleSet): void {
    this.editForm.patchValue({
      id: portalLintRuleSet.id,
      name: portalLintRuleSet.name,
      portal: portalLintRuleSet.portal,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const portalLintRuleSet = this.createFromForm();
    if (portalLintRuleSet.id !== undefined) {
      this.subscribeToSaveResponse(this.portalLintRuleSetService.update(portalLintRuleSet));
    } else {
      this.subscribeToSaveResponse(this.portalLintRuleSetService.create(portalLintRuleSet));
    }
  }

  private createFromForm(): IPortalLintRuleSet {
    return {
      ...new PortalLintRuleSet(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      portal: this.editForm.get(['portal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortalLintRuleSet>>): void {
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
