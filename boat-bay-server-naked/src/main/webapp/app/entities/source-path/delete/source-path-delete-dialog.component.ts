import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISourcePath } from '../source-path.model';
import { SourcePathService } from '../service/source-path.service';

@Component({
  templateUrl: './source-path-delete-dialog.component.html',
})
export class SourcePathDeleteDialogComponent {
  sourcePath?: ISourcePath;

  constructor(protected sourcePathService: SourcePathService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourcePathService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
