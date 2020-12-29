import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPortalLintRuleConfig, PortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';
import { PortalLintRuleConfigService } from './portal-lint-rule-config.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from 'app/entities/portal-lint-rule/portal-lint-rule.service';

@Component({
  selector: 'jhi-portal-lint-rule-config-update',
  templateUrl: './portal-lint-rule-config-update.component.html',
})
export class PortalLintRuleConfigUpdateComponent implements OnInit {
  isSaving = false;
  portallintrules: IPortalLintRule[] = [];

  editForm = this.fb.group({
    id: [],
    path: [null, [Validators.required]],
    type: [null, [Validators.required]],
    value: [null, [Validators.required]],
    portalLintRule: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected portalLintRuleConfigService: PortalLintRuleConfigService,
    protected portalLintRuleService: PortalLintRuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRuleConfig }) => {
      this.updateForm(portalLintRuleConfig);

      this.portalLintRuleService
        .query({ filter: 'portallintruleconfig-is-null' })
        .pipe(
          map((res: HttpResponse<IPortalLintRule[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPortalLintRule[]) => {
          if (!portalLintRuleConfig.portalLintRule || !portalLintRuleConfig.portalLintRule.id) {
            this.portallintrules = resBody;
          } else {
            this.portalLintRuleService
              .find(portalLintRuleConfig.portalLintRule.id)
              .pipe(
                map((subRes: HttpResponse<IPortalLintRule>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPortalLintRule[]) => (this.portallintrules = concatRes));
          }
        });
    });
  }

  updateForm(portalLintRuleConfig: IPortalLintRuleConfig): void {
    this.editForm.patchValue({
      id: portalLintRuleConfig.id,
      path: portalLintRuleConfig.path,
      type: portalLintRuleConfig.type,
      value: portalLintRuleConfig.value,
      portalLintRule: portalLintRuleConfig.portalLintRule,
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
    const portalLintRuleConfig = this.createFromForm();
    if (portalLintRuleConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.portalLintRuleConfigService.update(portalLintRuleConfig));
    } else {
      this.subscribeToSaveResponse(this.portalLintRuleConfigService.create(portalLintRuleConfig));
    }
  }

  private createFromForm(): IPortalLintRuleConfig {
    return {
      ...new PortalLintRuleConfig(),
      id: this.editForm.get(['id'])!.value,
      path: this.editForm.get(['path'])!.value,
      type: this.editForm.get(['type'])!.value,
      value: this.editForm.get(['value'])!.value,
      portalLintRule: this.editForm.get(['portalLintRule'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortalLintRuleConfig>>): void {
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

  trackById(index: number, item: IPortalLintRule): any {
    return item.id;
  }
}
