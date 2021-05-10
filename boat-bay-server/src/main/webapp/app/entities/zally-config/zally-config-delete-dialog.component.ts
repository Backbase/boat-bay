import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IZallyConfig } from 'app/shared/model/zally-config.model';
import { ZallyConfigService } from './zally-config.service';

@Component({
  templateUrl: './zally-config-delete-dialog.component.html',
})
export class ZallyConfigDeleteDialogComponent {
  zallyConfig?: IZallyConfig;

  constructor(
    protected zallyConfigService: ZallyConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zallyConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('zallyConfigListModification');
      this.activeModal.close();
    });
  }
}
