import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISource } from 'app/shared/model/source.model';
import { SourceService } from './source.service';
import { SourceDeleteDialogComponent } from './source-delete-dialog.component';

@Component({
  selector: 'jhi-source',
  templateUrl: './source.component.html',
})
export class SourceComponent implements OnInit, OnDestroy {
  sources?: ISource[];
  eventSubscriber?: Subscription;

  constructor(protected sourceService: SourceService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.sourceService.query().subscribe((res: HttpResponse<ISource[]>) => (this.sources = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSources();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISource): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSources(): void {
    this.eventSubscriber = this.eventManager.subscribe('sourceListModification', () => this.loadAll());
  }

  delete(source: ISource): void {
    const modalRef = this.modalService.open(SourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.source = source;
  }
}
