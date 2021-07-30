import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICapability } from '../capability.model';
import { CapabilityService } from '../service/capability.service';

@Component({
  templateUrl: './capability-delete-dialog.component.html',
})
export class CapabilityDeleteDialogComponent {
  capability?: ICapability;

  constructor(protected capabilityService: CapabilityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.capabilityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
