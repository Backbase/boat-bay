import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUpload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';

@Component({
  templateUrl: './upload-delete-dialog.component.html',
})
export class UploadDeleteDialogComponent {
  upload?: IUpload;

  constructor(protected uploadService: UploadService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uploadService.delete(id).subscribe(() => {
      this.eventManager.broadcast('uploadListModification');
      this.activeModal.close();
    });
  }
}
