import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from './portal.service';
import { PortalDeleteDialogComponent } from './portal-delete-dialog.component';

@Component({
  selector: 'jhi-portal',
  templateUrl: './portal.component.html',
})
export class PortalComponent implements OnInit, OnDestroy {
  portals?: IPortal[];
  eventSubscriber?: Subscription;

  constructor(protected portalService: PortalService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPortals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPortal): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPortals(): void {
    this.eventSubscriber = this.eventManager.subscribe('portalListModification', () => this.loadAll());
  }

  delete(portal: IPortal): void {
    const modalRef = this.modalService.open(PortalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.portal = portal;
  }
}
