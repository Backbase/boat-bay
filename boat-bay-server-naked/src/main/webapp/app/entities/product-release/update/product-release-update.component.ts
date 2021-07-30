import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProductRelease, ProductRelease } from '../product-release.model';
import { ProductReleaseService } from '../service/product-release.service';
import { ISpec } from 'app/entities/spec/spec.model';
import { SpecService } from 'app/entities/spec/service/spec.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-product-release-update',
  templateUrl: './product-release-update.component.html',
})
export class ProductReleaseUpdateComponent implements OnInit {
  isSaving = false;

  specsSharedCollection: ISpec[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    version: [null, [Validators.required]],
    releaseDate: [],
    hide: [],
    specs: [],
    product: [null, Validators.required],
  });

  constructor(
    protected productReleaseService: ProductReleaseService,
    protected specService: SpecService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productRelease }) => {
      if (productRelease.id === undefined) {
        const today = dayjs().startOf('day');
        productRelease.releaseDate = today;
      }

      this.updateForm(productRelease);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productRelease = this.createFromForm();
    if (productRelease.id !== undefined) {
      this.subscribeToSaveResponse(this.productReleaseService.update(productRelease));
    } else {
      this.subscribeToSaveResponse(this.productReleaseService.create(productRelease));
    }
  }

  trackSpecById(index: number, item: ISpec): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  getSelectedSpec(option: ISpec, selectedVals?: ISpec[]): ISpec {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductRelease>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productRelease: IProductRelease): void {
    this.editForm.patchValue({
      id: productRelease.id,
      key: productRelease.key,
      name: productRelease.name,
      version: productRelease.version,
      releaseDate: productRelease.releaseDate ? productRelease.releaseDate.format(DATE_TIME_FORMAT) : null,
      hide: productRelease.hide,
      specs: productRelease.specs,
      product: productRelease.product,
    });

    this.specsSharedCollection = this.specService.addSpecToCollectionIfMissing(this.specsSharedCollection, ...(productRelease.specs ?? []));
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      productRelease.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.specService
      .query()
      .pipe(map((res: HttpResponse<ISpec[]>) => res.body ?? []))
      .pipe(map((specs: ISpec[]) => this.specService.addSpecToCollectionIfMissing(specs, ...(this.editForm.get('specs')!.value ?? []))))
      .subscribe((specs: ISpec[]) => (this.specsSharedCollection = specs));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IProductRelease {
    return {
      ...new ProductRelease(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      version: this.editForm.get(['version'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? dayjs(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      hide: this.editForm.get(['hide'])!.value,
      specs: this.editForm.get(['specs'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
