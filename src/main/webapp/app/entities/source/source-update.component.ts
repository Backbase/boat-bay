import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ISource, Source } from 'app/shared/model/source.model';
import { SourceService } from './source.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from 'app/entities/capability-service-definition/capability-service-definition.service';

type SelectableEntity = IPortal | ICapability | ICapabilityServiceDefinition;

@Component({
  selector: 'jhi-source-update',
  templateUrl: './source-update.component.html',
})
export class SourceUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];
  capabilities: ICapability[] = [];
  capabilityservicedefinitions: ICapabilityServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    baseUrl: [null, [Validators.required]],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    active: [],
    path: [],
    filter: [],
    username: [],
    password: [],
    cronExpression: [],
    capabilityNameSpEL: [],
    serviceNameSpEL: [],
    versionSpEL: [],
    portal: [null, Validators.required],
    capability: [],
    capabilityServiceDefinition: [],
  });

  constructor(
    protected sourceService: SourceService,
    protected portalService: PortalService,
    protected capabilityService: CapabilityService,
    protected capabilityServiceDefinitionService: CapabilityServiceDefinitionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ source }) => {
      this.updateForm(source);

      this.portalService
        .query({ filter: 'source-is-null' })
        .pipe(
          map((res: HttpResponse<IPortal[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPortal[]) => {
          if (!source.portal || !source.portal.id) {
            this.portals = resBody;
          } else {
            this.portalService
              .find(source.portal.id)
              .pipe(
                map((subRes: HttpResponse<IPortal>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPortal[]) => (this.portals = concatRes));
          }
        });

      this.capabilityService
        .query({ filter: 'source-is-null' })
        .pipe(
          map((res: HttpResponse<ICapability[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICapability[]) => {
          if (!source.capability || !source.capability.id) {
            this.capabilities = resBody;
          } else {
            this.capabilityService
              .find(source.capability.id)
              .pipe(
                map((subRes: HttpResponse<ICapability>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICapability[]) => (this.capabilities = concatRes));
          }
        });

      this.capabilityServiceDefinitionService
        .query({ filter: 'source-is-null' })
        .pipe(
          map((res: HttpResponse<ICapabilityServiceDefinition[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICapabilityServiceDefinition[]) => {
          if (!source.capabilityServiceDefinition || !source.capabilityServiceDefinition.id) {
            this.capabilityservicedefinitions = resBody;
          } else {
            this.capabilityServiceDefinitionService
              .find(source.capabilityServiceDefinition.id)
              .pipe(
                map((subRes: HttpResponse<ICapabilityServiceDefinition>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICapabilityServiceDefinition[]) => (this.capabilityservicedefinitions = concatRes));
          }
        });
    });
  }

  updateForm(source: ISource): void {
    this.editForm.patchValue({
      id: source.id,
      baseUrl: source.baseUrl,
      name: source.name,
      type: source.type,
      active: source.active,
      path: source.path,
      filter: source.filter,
      username: source.username,
      password: source.password,
      cronExpression: source.cronExpression,
      capabilityNameSpEL: source.capabilityNameSpEL,
      serviceNameSpEL: source.serviceNameSpEL,
      versionSpEL: source.versionSpEL,
      portal: source.portal,
      capability: source.capability,
      capabilityServiceDefinition: source.capabilityServiceDefinition,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const source = this.createFromForm();
    if (source.id !== undefined) {
      this.subscribeToSaveResponse(this.sourceService.update(source));
    } else {
      this.subscribeToSaveResponse(this.sourceService.create(source));
    }
  }

  private createFromForm(): ISource {
    return {
      ...new Source(),
      id: this.editForm.get(['id'])!.value,
      baseUrl: this.editForm.get(['baseUrl'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      active: this.editForm.get(['active'])!.value,
      path: this.editForm.get(['path'])!.value,
      filter: this.editForm.get(['filter'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      cronExpression: this.editForm.get(['cronExpression'])!.value,
      capabilityNameSpEL: this.editForm.get(['capabilityNameSpEL'])!.value,
      serviceNameSpEL: this.editForm.get(['serviceNameSpEL'])!.value,
      versionSpEL: this.editForm.get(['versionSpEL'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      capabilityServiceDefinition: this.editForm.get(['capabilityServiceDefinition'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISource>>): void {
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
