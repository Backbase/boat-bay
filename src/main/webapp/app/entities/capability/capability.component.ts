import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from './capability.service';
import { CapabilityDeleteDialogComponent } from './capability-delete-dialog.component';

@Component({
  selector: 'jhi-capability',
  templateUrl: './capability.component.html',
})
export class CapabilityComponent implements OnInit, OnDestroy {
  capabilities?: ICapability[];
  eventSubscriber?: Subscription;

  constructor(protected capabilityService: CapabilityService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCapabilities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICapability): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCapabilities(): void {
    this.eventSubscriber = this.eventManager.subscribe('capabilityListModification', () => this.loadAll());
  }

  delete(capability: ICapability): void {
    const modalRef = this.modalService.open(CapabilityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.capability = capability;
  }
}
