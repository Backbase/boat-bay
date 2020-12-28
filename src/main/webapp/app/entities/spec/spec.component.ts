import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpec } from 'app/shared/model/spec.model';
import { SpecService } from './spec.service';
import { SpecDeleteDialogComponent } from './spec-delete-dialog.component';

@Component({
  selector: 'jhi-spec',
  templateUrl: './spec.component.html',
})
export class SpecComponent implements OnInit, OnDestroy {
  specs?: ISpec[];
  eventSubscriber?: Subscription;

  constructor(
    protected specService: SpecService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.specService.query().subscribe((res: HttpResponse<ISpec[]>) => (this.specs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSpecs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISpec): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInSpecs(): void {
    this.eventSubscriber = this.eventManager.subscribe('specListModification', () => this.loadAll());
  }

  delete(spec: ISpec): void {
    const modalRef = this.modalService.open(SpecDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.spec = spec;
  }
}
