import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductRelease } from '../product-release.model';
import { ProductReleaseService } from '../service/product-release.service';
import { ProductReleaseDeleteDialogComponent } from '../delete/product-release-delete-dialog.component';

@Component({
  selector: 'jhi-product-release',
  templateUrl: './product-release.component.html',
})
export class ProductReleaseComponent implements OnInit {
  productReleases?: IProductRelease[];
  isLoading = false;

  constructor(protected productReleaseService: ProductReleaseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productReleaseService.query().subscribe(
      (res: HttpResponse<IProductRelease[]>) => {
        this.isLoading = false;
        this.productReleases = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProductRelease): number {
    return item.id!;
  }

  delete(productRelease: IProductRelease): void {
    const modalRef = this.modalService.open(ProductReleaseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productRelease = productRelease;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
