import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';
import { UploadDeleteDialogComponent } from './upload-delete-dialog.component';

@Component({
  selector: 'jhi-upload',
  templateUrl: './upload.component.html',
})
export class UploadComponent implements OnInit, OnDestroy {
  uploads?: IUpload[];
  eventSubscriber?: Subscription;

  constructor(
    protected uploadService: UploadService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.uploadService.query().subscribe((res: HttpResponse<IUpload[]>) => (this.uploads = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUploads();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUpload): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInUploads(): void {
    this.eventSubscriber = this.eventManager.subscribe('uploadListModification', () => this.loadAll());
  }

  delete(upload: IUpload): void {
    const modalRef = this.modalService.open(UploadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.upload = upload;
  }
}
