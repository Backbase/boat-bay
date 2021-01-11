import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPortal, Portal } from 'app/shared/model/portal.model';
import { PortalService } from './portal.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

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
    title: [],
    subTitle: [],
    navTitle: [],
    logoUrl: [],
    logoLink: [],
    content: [],
    createdOn: [],
    createdBy: [],
    hide: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portal }) => {
      if (!portal.id) {
        const today = moment().startOf('day');
        portal.createdOn = today;
      }

      this.updateForm(portal);
    });
  }

  updateForm(portal: IPortal): void {
    this.editForm.patchValue({
      id: portal.id,
      key: portal.key,
      name: portal.name,
      title: portal.title,
      subTitle: portal.subTitle,
      navTitle: portal.navTitle,
      logoUrl: portal.logoUrl,
      logoLink: portal.logoLink,
      content: portal.content,
      createdOn: portal.createdOn ? portal.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: portal.createdBy,
      hide: portal.hide,
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
    const portal = this.createFromForm();
    if (portal.id !== undefined) {
      this.subscribeToSaveResponse(this.portalService.update(portal));
    } else {
      this.subscribeToSaveResponse(this.portalService.create(portal));
    }
  }

  private createFromForm(): IPortal {
    return {
      ...new Portal(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      navTitle: this.editForm.get(['navTitle'])!.value,
      logoUrl: this.editForm.get(['logoUrl'])!.value,
      logoLink: this.editForm.get(['logoLink'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      hide: this.editForm.get(['hide'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortal>>): void {
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
}
