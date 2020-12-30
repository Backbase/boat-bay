import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICapability, Capability } from 'app/shared/model/capability.model';
import { CapabilityService } from './capability.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-capability-update',
  templateUrl: './capability-update.component.html',
})
export class CapabilityUpdateComponent implements OnInit {
  isSaving = false;
  products: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    title: [],
    subTitle: [],
    navTitle: [],
    content: [],
    version: [],
    createdOn: [],
    createdBy: [],
    product: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected capabilityService: CapabilityService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capability }) => {
      if (!capability.id) {
        const today = moment().startOf('day');
        capability.createdOn = today;
      }

      this.updateForm(capability);

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));
    });
  }

  updateForm(capability: ICapability): void {
    this.editForm.patchValue({
      id: capability.id,
      key: capability.key,
      name: capability.name,
      title: capability.title,
      subTitle: capability.subTitle,
      navTitle: capability.navTitle,
      content: capability.content,
      version: capability.version,
      createdOn: capability.createdOn ? capability.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: capability.createdBy,
      product: capability.product,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('boatBayApp.error', { message: err.message })
      );
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

  private createFromForm(): ICapability {
    return {
      ...new Capability(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      navTitle: this.editForm.get(['navTitle'])!.value,
      content: this.editForm.get(['content'])!.value,
      version: this.editForm.get(['version'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICapability>>): void {
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

  trackById(index: number, item: IProduct): any {
    return item.id;
  }
}
