import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISpecType } from 'app/shared/model/spec-type.model';
import { SpecTypeService } from './spec-type.service';

@Component({
  templateUrl: './spec-type-delete-dialog.component.html',
})
export class SpecTypeDeleteDialogComponent {
  specType?: ISpecType;

  constructor(protected specTypeService: SpecTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('specTypeListModification');
      this.activeModal.close();
    });
  }
}
