import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISource } from 'app/shared/model/source.model';
import { SourceService } from './source.service';

@Component({
  templateUrl: './source-delete-dialog.component.html',
})
export class SourceDeleteDialogComponent {
  source?: ISource;

  constructor(protected sourceService: SourceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sourceListModification');
      this.activeModal.close();
    });
  }
}
