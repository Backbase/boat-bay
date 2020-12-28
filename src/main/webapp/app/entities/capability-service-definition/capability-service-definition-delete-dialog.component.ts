import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from './capability-service-definition.service';

@Component({
  templateUrl: './capability-service-definition-delete-dialog.component.html',
})
export class CapabilityServiceDefinitionDeleteDialogComponent {
  capabilityServiceDefinition?: ICapabilityServiceDefinition;

  constructor(
    protected capabilityServiceDefinitionService: CapabilityServiceDefinitionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.capabilityServiceDefinitionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('capabilityServiceDefinitionListModification');
      this.activeModal.close();
    });
  }
}
