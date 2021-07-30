import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecType } from '../spec-type.model';
import { SpecTypeService } from '../service/spec-type.service';

@Component({
  templateUrl: './spec-type-delete-dialog.component.html',
})
export class SpecTypeDeleteDialogComponent {
  specType?: ISpecType;

  constructor(protected specTypeService: SpecTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
