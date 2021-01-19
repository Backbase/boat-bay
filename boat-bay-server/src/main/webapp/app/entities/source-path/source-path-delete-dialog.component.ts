import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISourcePath } from 'app/shared/model/source-path.model';
import { SourcePathService } from './source-path.service';

@Component({
  templateUrl: './source-path-delete-dialog.component.html',
})
export class SourcePathDeleteDialogComponent {
  sourcePath?: ISourcePath;

  constructor(
    protected sourcePathService: SourcePathService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourcePathService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sourcePathListModification');
      this.activeModal.close();
    });
  }
}
