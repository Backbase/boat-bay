import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductRelease } from 'app/shared/model/product-release.model';
import { ProductReleaseService } from './product-release.service';

@Component({
  templateUrl: './product-release-delete-dialog.component.html',
})
export class ProductReleaseDeleteDialogComponent {
  productRelease?: IProductRelease;

  constructor(
    protected productReleaseService: ProductReleaseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productReleaseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('productReleaseListModification');
      this.activeModal.close();
    });
  }
}
