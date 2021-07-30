import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { ProductDeleteDialogComponent } from '../delete/product-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit {
  products?: IProduct[];
  isLoading = false;

  constructor(protected productService: ProductService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productService.query().subscribe(
      (res: HttpResponse<IProduct[]>) => {
        this.isLoading = false;
        this.products = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProduct): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
