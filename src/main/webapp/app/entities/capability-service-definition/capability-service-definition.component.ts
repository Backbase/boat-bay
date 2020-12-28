import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from './capability-service-definition.service';
import { CapabilityServiceDefinitionDeleteDialogComponent } from './capability-service-definition-delete-dialog.component';

@Component({
  selector: 'jhi-capability-service-definition',
  templateUrl: './capability-service-definition.component.html',
})
export class CapabilityServiceDefinitionComponent implements OnInit, OnDestroy {
  capabilityServiceDefinitions?: ICapabilityServiceDefinition[];
  eventSubscriber?: Subscription;

  constructor(
    protected capabilityServiceDefinitionService: CapabilityServiceDefinitionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.capabilityServiceDefinitionService
      .query()
      .subscribe((res: HttpResponse<ICapabilityServiceDefinition[]>) => (this.capabilityServiceDefinitions = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCapabilityServiceDefinitions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICapabilityServiceDefinition): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCapabilityServiceDefinitions(): void {
    this.eventSubscriber = this.eventManager.subscribe('capabilityServiceDefinitionListModification', () => this.loadAll());
  }

  delete(capabilityServiceDefinition: ICapabilityServiceDefinition): void {
    const modalRef = this.modalService.open(CapabilityServiceDefinitionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.capabilityServiceDefinition = capabilityServiceDefinition;
  }
}
