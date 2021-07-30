import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISource } from '../source.model';
import { SourceService } from '../service/source.service';

@Component({
  templateUrl: './source-delete-dialog.component.html',
})
export class SourceDeleteDialogComponent {
  source?: ISource;

  constructor(protected sourceService: SourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
