import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from './service-definition.service';

@Component({
  templateUrl: './service-definition-delete-dialog.component.html',
})
export class ServiceDefinitionDeleteDialogComponent {
  serviceDefinition?: IServiceDefinition;

  constructor(
    protected serviceDefinitionService: ServiceDefinitionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceDefinitionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('serviceDefinitionListModification');
      this.activeModal.close();
    });
  }
}
