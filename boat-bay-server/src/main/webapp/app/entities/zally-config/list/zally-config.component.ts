import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IZallyConfig } from '../zally-config.model';
import { ZallyConfigService } from '../service/zally-config.service';
import { ZallyConfigDeleteDialogComponent } from '../delete/zally-config-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-zally-config',
  templateUrl: './zally-config.component.html',
})
export class ZallyConfigComponent implements OnInit {
  zallyConfigs?: IZallyConfig[];
  isLoading = false;

  constructor(protected zallyConfigService: ZallyConfigService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.zallyConfigService.query().subscribe(
      (res: HttpResponse<IZallyConfig[]>) => {
        this.isLoading = false;
        this.zallyConfigs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IZallyConfig): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(zallyConfig: IZallyConfig): void {
    const modalRef = this.modalService.open(ZallyConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.zallyConfig = zallyConfig;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
