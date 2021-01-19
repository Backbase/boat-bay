import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from './portal.service';

@Component({
  templateUrl: './portal-delete-dialog.component.html',
})
export class PortalDeleteDialogComponent {
  portal?: IPortal;

  constructor(protected portalService: PortalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portalService.delete(id).subscribe(() => {
      this.eventManager.broadcast('portalListModification');
      this.activeModal.close();
    });
  }
}
