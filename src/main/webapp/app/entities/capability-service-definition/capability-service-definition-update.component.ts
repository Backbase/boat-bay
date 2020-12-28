import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICapabilityServiceDefinition, CapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from './capability-service-definition.service';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';

@Component({
  selector: 'jhi-capability-service-definition-update',
  templateUrl: './capability-service-definition-update.component.html',
})
export class CapabilityServiceDefinitionUpdateComponent implements OnInit {
  isSaving = false;
  capabilities: ICapability[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    title: [],
    subTitle: [],
    navTitle: [],
    content: [],
    createdOn: [],
    createdBy: [],
    capability: [null, Validators.required],
  });

  constructor(
    protected capabilityServiceDefinitionService: CapabilityServiceDefinitionService,
    protected capabilityService: CapabilityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capabilityServiceDefinition }) => {
      if (!capabilityServiceDefinition.id) {
        const today = moment().startOf('day');
        capabilityServiceDefinition.createdOn = today;
      }

      this.updateForm(capabilityServiceDefinition);

      this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));
    });
  }

  updateForm(capabilityServiceDefinition: ICapabilityServiceDefinition): void {
    this.editForm.patchValue({
      id: capabilityServiceDefinition.id,
      key: capabilityServiceDefinition.key,
      name: capabilityServiceDefinition.name,
      title: capabilityServiceDefinition.title,
      subTitle: capabilityServiceDefinition.subTitle,
      navTitle: capabilityServiceDefinition.navTitle,
      content: capabilityServiceDefinition.content,
      createdOn: capabilityServiceDefinition.createdOn ? capabilityServiceDefinition.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: capabilityServiceDefinition.createdBy,
      capability: capabilityServiceDefinition.capability,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const capabilityServiceDefinition = this.createFromForm();
    if (capabilityServiceDefinition.id !== undefined) {
      this.subscribeToSaveResponse(this.capabilityServiceDefinitionService.update(capabilityServiceDefinition));
    } else {
      this.subscribeToSaveResponse(this.capabilityServiceDefinitionService.create(capabilityServiceDefinition));
    }
  }

  private createFromForm(): ICapabilityServiceDefinition {
    return {
      ...new CapabilityServiceDefinition(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      navTitle: this.editForm.get(['navTitle'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      capability: this.editForm.get(['capability'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICapabilityServiceDefinition>>): void {
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
