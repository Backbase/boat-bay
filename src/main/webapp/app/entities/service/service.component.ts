import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IService } from 'app/shared/model/service.model';
import { ServiceService } from './service.service';
import { ServiceDeleteDialogComponent } from './service-delete-dialog.component';

@Component({
  selector: 'jhi-service',
  templateUrl: './service.component.html',
})
export class ServiceComponent implements OnInit, OnDestroy {
  services?: IService[];
  eventSubscriber?: Subscription;

  constructor(protected serviceService: ServiceService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.serviceService.query().subscribe((res: HttpResponse<IService[]>) => (this.services = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInServices();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IService): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInServices(): void {
    this.eventSubscriber = this.eventManager.subscribe('serviceListModification', () => this.loadAll());
  }

  delete(service: IService): void {
    const modalRef = this.modalService.open(ServiceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.service = service;
  }
}
