import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IServiceDefinition, ServiceDefinition } from '../service-definition.model';
import { ServiceDefinitionService } from '../service/service-definition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';

@Component({
  selector: 'jhi-service-definition-update',
  templateUrl: './service-definition-update.component.html',
})
export class ServiceDefinitionUpdateComponent implements OnInit {
  isSaving = false;

  capabilitiesSharedCollection: ICapability[] = [];

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
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected capabilityService: CapabilityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceDefinition }) => {
      if (serviceDefinition.id === undefined) {
        const today = dayjs().startOf('day');
        serviceDefinition.createdOn = today;
      }

      this.updateForm(serviceDefinition);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('boatbayApp.error', { ...err, key: 'error.file.' + err.key })),
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

  trackCapabilityById(index: number, item: ICapability): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceDefinition>>): void {
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

  protected updateForm(serviceDefinition: IServiceDefinition): void {
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

    this.capabilitiesSharedCollection = this.capabilityService.addCapabilityToCollectionIfMissing(
      this.capabilitiesSharedCollection,
      serviceDefinition.capability
    );
  }

  protected loadRelationshipsOptions(): void {
    this.capabilityService
      .query()
      .pipe(map((res: HttpResponse<ICapability[]>) => res.body ?? []))
      .pipe(
        map((capabilities: ICapability[]) =>
          this.capabilityService.addCapabilityToCollectionIfMissing(capabilities, this.editForm.get('capability')!.value)
        )
      )
      .subscribe((capabilities: ICapability[]) => (this.capabilitiesSharedCollection = capabilities));
  }

  protected createFromForm(): IServiceDefinition {
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
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      hide: this.editForm.get(['hide'])!.value,
      capability: this.editForm.get(['capability'])!.value,
    };
  }
}
