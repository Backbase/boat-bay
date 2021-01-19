import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { ProductDeleteDialogComponent } from './product-delete-dialog.component';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit, OnDestroy {
  products?: IProduct[];
  eventSubscriber?: Subscription;

  constructor(
    protected productService: ProductService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProducts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProduct): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInProducts(): void {
    this.eventSubscriber = this.eventManager.subscribe('productListModification', () => this.loadAll());
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
  }
}
