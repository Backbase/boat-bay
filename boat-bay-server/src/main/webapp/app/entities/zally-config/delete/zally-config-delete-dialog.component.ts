import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IZallyConfig } from '../zally-config.model';
import { ZallyConfigService } from '../service/zally-config.service';

@Component({
  templateUrl: './zally-config-delete-dialog.component.html',
})
export class ZallyConfigDeleteDialogComponent {
  zallyConfig?: IZallyConfig;

  constructor(protected zallyConfigService: ZallyConfigService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zallyConfigService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
