import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductRelease } from 'app/shared/model/product-release.model';
import { ProductReleaseService } from './product-release.service';
import { ProductReleaseDeleteDialogComponent } from './product-release-delete-dialog.component';

@Component({
  selector: 'jhi-product-release',
  templateUrl: './product-release.component.html',
})
export class ProductReleaseComponent implements OnInit, OnDestroy {
  productReleases?: IProductRelease[];
  eventSubscriber?: Subscription;

  constructor(
    protected productReleaseService: ProductReleaseService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.productReleaseService.query().subscribe((res: HttpResponse<IProductRelease[]>) => (this.productReleases = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProductReleases();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProductRelease): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProductReleases(): void {
    this.eventSubscriber = this.eventManager.subscribe('productReleaseListModification', () => this.loadAll());
  }

  delete(productRelease: IProductRelease): void {
    const modalRef = this.modalService.open(ProductReleaseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productRelease = productRelease;
  }
}
