import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICapability, Capability } from '../capability.model';
import { CapabilityService } from '../service/capability.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-capability-update',
  templateUrl: './capability-update.component.html',
})
export class CapabilityUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    order: [],
    subTitle: [],
    content: [],
    createdOn: [],
    createdBy: [],
    hide: [],
    product: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected capabilityService: CapabilityService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capability }) => {
      if (capability.id === undefined) {
        const today = dayjs().startOf('day');
        capability.createdOn = today;
      }

      this.updateForm(capability);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('boatbayApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const capability = this.createFromForm();
    if (capability.id !== undefined) {
      this.subscribeToSaveResponse(this.capabilityService.update(capability));
    } else {
      this.subscribeToSaveResponse(this.capabilityService.create(capability));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICapability>>): void {
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

  protected updateForm(capability: ICapability): void {
    this.editForm.patchValue({
      id: capability.id,
      key: capability.key,
      name: capability.name,
      order: capability.order,
      subTitle: capability.subTitle,
      content: capability.content,
      createdOn: capability.createdOn ? capability.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: capability.createdBy,
      hide: capability.hide,
      product: capability.product,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, capability.product);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): ICapability {
    return {
      ...new Capability(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      order: this.editForm.get(['order'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      hide: this.editForm.get(['hide'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
