import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpec } from '../spec.model';
import { SpecService } from '../service/spec.service';

@Component({
  templateUrl: './spec-delete-dialog.component.html',
})
export class SpecDeleteDialogComponent {
  spec?: ISpec;

  constructor(protected specService: SpecService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
