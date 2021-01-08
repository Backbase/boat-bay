import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { TagDeleteDialogComponent } from './tag-delete-dialog.component';

@Component({
  selector: 'jhi-tag',
  templateUrl: './tag.component.html',
})
export class TagComponent implements OnInit, OnDestroy {
  tags?: ITag[];
  eventSubscriber?: Subscription;

  constructor(
    protected tagService: TagService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => (this.tags = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTags();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITag): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInTags(): void {
    this.eventSubscriber = this.eventManager.subscribe('tagListModification', () => this.loadAll());
  }

  delete(tag: ITag): void {
    const modalRef = this.modalService.open(TagDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tag = tag;
  }
}
