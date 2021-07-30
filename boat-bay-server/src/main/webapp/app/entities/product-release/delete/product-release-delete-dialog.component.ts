import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductRelease } from '../product-release.model';
import { ProductReleaseService } from '../service/product-release.service';

@Component({
  templateUrl: './product-release-delete-dialog.component.html',
})
export class ProductReleaseDeleteDialogComponent {
  productRelease?: IProductRelease;

  constructor(protected productReleaseService: ProductReleaseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productReleaseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
