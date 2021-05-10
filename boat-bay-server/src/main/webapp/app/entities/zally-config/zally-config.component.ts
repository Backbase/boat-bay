import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IZallyConfig } from 'app/shared/model/zally-config.model';
import { ZallyConfigService } from './zally-config.service';
import { ZallyConfigDeleteDialogComponent } from './zally-config-delete-dialog.component';

@Component({
  selector: 'jhi-zally-config',
  templateUrl: './zally-config.component.html',
})
export class ZallyConfigComponent implements OnInit, OnDestroy {
  zallyConfigs?: IZallyConfig[];
  eventSubscriber?: Subscription;

  constructor(
    protected zallyConfigService: ZallyConfigService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.zallyConfigService.query().subscribe((res: HttpResponse<IZallyConfig[]>) => (this.zallyConfigs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInZallyConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IZallyConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInZallyConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('zallyConfigListModification', () => this.loadAll());
  }

  delete(zallyConfig: IZallyConfig): void {
    const modalRef = this.modalService.open(ZallyConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.zallyConfig = zallyConfig;
  }
}
