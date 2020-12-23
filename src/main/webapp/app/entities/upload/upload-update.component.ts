import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IUpload, Upload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-upload-update',
  templateUrl: './upload-update.component.html',
})
export class UploadUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    createdOn: [],
    createdBy: [],
    file: [],
    fileContentType: [],
    fileName: [],
    processed: [],
    action: [],
    error: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected uploadService: UploadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ upload }) => {
      if (!upload.id) {
        const today = moment().startOf('day');
        upload.createdOn = today;
      }

      this.updateForm(upload);
    });
  }

  updateForm(upload: IUpload): void {
    this.editForm.patchValue({
      id: upload.id,
      createdOn: upload.createdOn ? upload.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: upload.createdBy,
      file: upload.file,
      fileContentType: upload.fileContentType,
      fileName: upload.fileName,
      processed: upload.processed,
      action: upload.action,
      error: upload.error,
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
    const upload = this.createFromForm();
    if (upload.id !== undefined) {
      this.subscribeToSaveResponse(this.uploadService.update(upload));
    } else {
      this.subscribeToSaveResponse(this.uploadService.create(upload));
    }
  }

  private createFromForm(): IUpload {
    return {
      ...new Upload(),
      id: this.editForm.get(['id'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      processed: this.editForm.get(['processed'])!.value,
      action: this.editForm.get(['action'])!.value,
      error: this.editForm.get(['error'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpload>>): void {
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
