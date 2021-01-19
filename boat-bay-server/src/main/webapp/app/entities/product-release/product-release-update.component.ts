import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProductRelease, ProductRelease } from 'app/shared/model/product-release.model';
import { ProductReleaseService } from './product-release.service';
import { ISpec } from 'app/shared/model/spec.model';
import { SpecService } from 'app/entities/spec/spec.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

type SelectableEntity = ISpec | IPortal;

@Component({
  selector: 'jhi-product-release-update',
  templateUrl: './product-release-update.component.html',
})
export class ProductReleaseUpdateComponent implements OnInit {
  isSaving = false;
  specs: ISpec[] = [];
  portals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    hide: [],
    specs: [],
    portal: [null, Validators.required],
  });

  constructor(
    protected productReleaseService: ProductReleaseService,
    protected specService: SpecService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productRelease }) => {
      this.updateForm(productRelease);

      this.specService.query().subscribe((res: HttpResponse<ISpec[]>) => (this.specs = res.body || []));

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));
    });
  }

  updateForm(productRelease: IProductRelease): void {
    this.editForm.patchValue({
      id: productRelease.id,
      key: productRelease.key,
      name: productRelease.name,
      hide: productRelease.hide,
      specs: productRelease.specs,
      portal: productRelease.portal,
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
      hide: this.editForm.get(['hide'])!.value,
      specs: this.editForm.get(['specs'])!.value,
      portal: this.editForm.get(['portal'])!.value,
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
