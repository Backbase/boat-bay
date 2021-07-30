import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPortal, Portal } from '../portal.model';
import { PortalService } from '../service/portal.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-portal-update',
  templateUrl: './portal-update.component.html',
})
export class PortalUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    subTitle: [],
    logoUrl: [],
    logoLink: [],
    content: [],
    createdOn: [],
    createdBy: [],
    hide: [],
    linted: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portal }) => {
      if (portal.id === undefined) {
        const today = dayjs().startOf('day');
        portal.createdOn = today;
      }

      this.updateForm(portal);
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
    const portal = this.createFromForm();
    if (portal.id !== undefined) {
      this.subscribeToSaveResponse(this.portalService.update(portal));
    } else {
      this.subscribeToSaveResponse(this.portalService.create(portal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortal>>): void {
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

  protected updateForm(portal: IPortal): void {
    this.editForm.patchValue({
      id: portal.id,
      key: portal.key,
      name: portal.name,
      subTitle: portal.subTitle,
      logoUrl: portal.logoUrl,
      logoLink: portal.logoLink,
      content: portal.content,
      createdOn: portal.createdOn ? portal.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: portal.createdBy,
      hide: portal.hide,
      linted: portal.linted,
    });
  }

  protected createFromForm(): IPortal {
    return {
      ...new Portal(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      logoUrl: this.editForm.get(['logoUrl'])!.value,
      logoLink: this.editForm.get(['logoLink'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      hide: this.editForm.get(['hide'])!.value,
      linted: this.editForm.get(['linted'])!.value,
    };
  }
}
