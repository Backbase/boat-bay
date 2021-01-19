import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductRelease } from 'app/shared/model/product-release.model';

@Component({
  selector: 'jhi-product-release-detail',
  templateUrl: './product-release-detail.component.html',
})
export class ProductReleaseDetailComponent implements OnInit {
  productRelease: IProductRelease | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productRelease }) => (this.productRelease = productRelease));
  }

  previousState(): void {
    window.history.back();
  }
}
