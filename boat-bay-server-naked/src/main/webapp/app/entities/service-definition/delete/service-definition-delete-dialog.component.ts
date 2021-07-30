import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceDefinition } from '../service-definition.model';
import { ServiceDefinitionService } from '../service/service-definition.service';

@Component({
  templateUrl: './service-definition-delete-dialog.component.html',
})
export class ServiceDefinitionDeleteDialogComponent {
  serviceDefinition?: IServiceDefinition;

  constructor(protected serviceDefinitionService: ServiceDefinitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceDefinitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
