import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IServiceDefinition, ServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from './service-definition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';

@Component({
  selector: 'jhi-service-definition-update',
  templateUrl: './service-definition-update.component.html',
})
export class ServiceDefinitionUpdateComponent implements OnInit {
  isSaving = false;
  capabilities: ICapability[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    order: [],
    subTitle: [],
    description: [],
    icon: [],
    color: [],
    createdOn: [],
    createdBy: [],
    hide: [],
    capability: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected capabilityService: CapabilityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceDefinition }) => {
      if (!serviceDefinition.id) {
        const today = moment().startOf('day');
        serviceDefinition.createdOn = today;
      }

      this.updateForm(serviceDefinition);

      this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));
    });
  }

  updateForm(serviceDefinition: IServiceDefinition): void {
    this.editForm.patchValue({
      id: serviceDefinition.id,
      key: serviceDefinition.key,
      name: serviceDefinition.name,
      order: serviceDefinition.order,
      subTitle: serviceDefinition.subTitle,
      description: serviceDefinition.description,
      icon: serviceDefinition.icon,
      color: serviceDefinition.color,
      createdOn: serviceDefinition.createdOn ? serviceDefinition.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: serviceDefinition.createdBy,
      hide: serviceDefinition.hide,
      capability: serviceDefinition.capability,
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
    const serviceDefinition = this.createFromForm();
    if (serviceDefinition.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceDefinitionService.update(serviceDefinition));
    } else {
      this.subscribeToSaveResponse(this.serviceDefinitionService.create(serviceDefinition));
    }
  }

  private createFromForm(): IServiceDefinition {
    return {
      ...new ServiceDefinition(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      order: this.editForm.get(['order'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      color: this.editForm.get(['color'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      hide: this.editForm.get(['hide'])!.value,
      capability: this.editForm.get(['capability'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceDefinition>>): void {
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

  trackById(index: number, item: ICapability): any {
    return item.id;
  }
}
