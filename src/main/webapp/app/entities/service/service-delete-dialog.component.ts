import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IService } from 'app/shared/model/service.model';
import { ServiceService } from './service.service';

@Component({
  templateUrl: './service-delete-dialog.component.html',
})
export class ServiceDeleteDialogComponent {
  service?: IService;

  constructor(protected serviceService: ServiceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('serviceListModification');
      this.activeModal.close();
    });
  }
}
