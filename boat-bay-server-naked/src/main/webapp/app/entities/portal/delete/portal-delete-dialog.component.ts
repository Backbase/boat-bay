import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortal } from '../portal.model';
import { PortalService } from '../service/portal.service';

@Component({
  templateUrl: './portal-delete-dialog.component.html',
})
export class PortalDeleteDialogComponent {
  portal?: IPortal;

  constructor(protected portalService: PortalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
