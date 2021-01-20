import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProductRelease, ProductRelease } from 'app/shared/model/product-release.model';
import { ProductReleaseService } from './product-release.service';
import { ISpec } from 'app/shared/model/spec.model';
import { SpecService } from 'app/entities/spec/spec.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

type SelectableEntity = ISpec | IProduct;

@Component({
  selector: 'jhi-product-release-update',
  templateUrl: './product-release-update.component.html',
})
export class ProductReleaseUpdateComponent implements OnInit {
  isSaving = false;
  specs: ISpec[] = [];
  products: IProduct[] = [];

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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productRelease }) => {
      if (!productRelease.id) {
        const today = moment().startOf('day');
        productRelease.releaseDate = today;
      }

      this.updateForm(productRelease);

      this.specService.query().subscribe((res: HttpResponse<ISpec[]>) => (this.specs = res.body || []));

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));
    });
  }

  updateForm(productRelease: IProductRelease): void {
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

  private createFromForm(): IProductRelease {
    return {
      ...new ProductRelease(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      version: this.editForm.get(['version'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? moment(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      hide: this.editForm.get(['hide'])!.value,
      specs: this.editForm.get(['specs'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductRelease>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: ISpec[], option: ISpec): ISpec {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
