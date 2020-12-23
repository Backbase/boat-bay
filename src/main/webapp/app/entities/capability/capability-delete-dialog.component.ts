import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from './capability.service';

@Component({
  templateUrl: './capability-delete-dialog.component.html',
})
export class CapabilityDeleteDialogComponent {
  capability?: ICapability;

  constructor(
    protected capabilityService: CapabilityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.capabilityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('capabilityListModification');
      this.activeModal.close();
    });
  }
}
