import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISpec } from 'app/shared/model/spec.model';
import { SpecService } from './spec.service';

@Component({
  templateUrl: './spec-delete-dialog.component.html',
})
export class SpecDeleteDialogComponent {
  spec?: ISpec;

  constructor(protected specService: SpecService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specService.delete(id).subscribe(() => {
      this.eventManager.broadcast('specListModification');
      this.activeModal.close();
    });
  }
}
