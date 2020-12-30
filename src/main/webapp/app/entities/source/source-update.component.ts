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
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service-definition.service';

type SelectableEntity = IPortal | ICapability | IServiceDefinition;

@Component({
  selector: 'jhi-source-update',
  templateUrl: './source-update.component.html',
})
export class SourceUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];
  capabilities: ICapability[] = [];
  servicedefinitions: IServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    baseUrl: [null, [Validators.required]],
    active: [],
    path: [],
    filter: [],
    username: [],
    password: [],
    cronExpression: [],
    specFilterSpEL: [],
    productKeySpEL: [],
    productNameSpEL: [],
    capabilityKeySpEL: [],
    capabilityNameSpEL: [],
    serviceKeySpEL: [],
    serviceNameSpEL: [],
    versionSpEL: [],
    overwriteChanges: [],
    portal: [null, Validators.required],
    capability: [],
    serviceDefinition: [],
  });

  constructor(
    protected sourceService: SourceService,
    protected portalService: PortalService,
    protected capabilityService: CapabilityService,
    protected serviceDefinitionService: ServiceDefinitionService,
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

      this.serviceDefinitionService
        .query({ filter: 'source-is-null' })
        .pipe(
          map((res: HttpResponse<IServiceDefinition[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IServiceDefinition[]) => {
          if (!source.serviceDefinition || !source.serviceDefinition.id) {
            this.servicedefinitions = resBody;
          } else {
            this.serviceDefinitionService
              .find(source.serviceDefinition.id)
              .pipe(
                map((subRes: HttpResponse<IServiceDefinition>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IServiceDefinition[]) => (this.servicedefinitions = concatRes));
          }
        });
    });
  }

  updateForm(source: ISource): void {
    this.editForm.patchValue({
      id: source.id,
      name: source.name,
      type: source.type,
      baseUrl: source.baseUrl,
      active: source.active,
      path: source.path,
      filter: source.filter,
      username: source.username,
      password: source.password,
      cronExpression: source.cronExpression,
      specFilterSpEL: source.specFilterSpEL,
      productKeySpEL: source.productKeySpEL,
      productNameSpEL: source.productNameSpEL,
      capabilityKeySpEL: source.capabilityKeySpEL,
      capabilityNameSpEL: source.capabilityNameSpEL,
      serviceKeySpEL: source.serviceKeySpEL,
      serviceNameSpEL: source.serviceNameSpEL,
      versionSpEL: source.versionSpEL,
      overwriteChanges: source.overwriteChanges,
      portal: source.portal,
      capability: source.capability,
      serviceDefinition: source.serviceDefinition,
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
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      baseUrl: this.editForm.get(['baseUrl'])!.value,
      active: this.editForm.get(['active'])!.value,
      path: this.editForm.get(['path'])!.value,
      filter: this.editForm.get(['filter'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      cronExpression: this.editForm.get(['cronExpression'])!.value,
      specFilterSpEL: this.editForm.get(['specFilterSpEL'])!.value,
      productKeySpEL: this.editForm.get(['productKeySpEL'])!.value,
      productNameSpEL: this.editForm.get(['productNameSpEL'])!.value,
      capabilityKeySpEL: this.editForm.get(['capabilityKeySpEL'])!.value,
      capabilityNameSpEL: this.editForm.get(['capabilityNameSpEL'])!.value,
      serviceKeySpEL: this.editForm.get(['serviceKeySpEL'])!.value,
      serviceNameSpEL: this.editForm.get(['serviceNameSpEL'])!.value,
      versionSpEL: this.editForm.get(['versionSpEL'])!.value,
      overwriteChanges: this.editForm.get(['overwriteChanges'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      serviceDefinition: this.editForm.get(['serviceDefinition'])!.value,
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
