import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISourcePath } from 'app/shared/model/source-path.model';
import { SourcePathService } from './source-path.service';
import { SourcePathDeleteDialogComponent } from './source-path-delete-dialog.component';

@Component({
  selector: 'jhi-source-path',
  templateUrl: './source-path.component.html',
})
export class SourcePathComponent implements OnInit, OnDestroy {
  sourcePaths?: ISourcePath[];
  eventSubscriber?: Subscription;

  constructor(protected sourcePathService: SourcePathService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.sourcePathService.query().subscribe((res: HttpResponse<ISourcePath[]>) => (this.sourcePaths = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSourcePaths();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISourcePath): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSourcePaths(): void {
    this.eventSubscriber = this.eventManager.subscribe('sourcePathListModification', () => this.loadAll());
  }

  delete(sourcePath: ISourcePath): void {
    const modalRef = this.modalService.open(SourcePathDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sourcePath = sourcePath;
  }
}
