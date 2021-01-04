import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISpec, Spec } from 'app/shared/model/spec.model';
import { SpecService } from './spec.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { ISource } from 'app/shared/model/source.model';
import { SourceService } from 'app/entities/source/source.service';
import { ISpecType } from 'app/shared/model/spec-type.model';
import { SpecTypeService } from 'app/entities/spec-type/spec-type.service';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service-definition.service';

type SelectableEntity = IPortal | ICapability | IProduct | ISource | ISpecType | IServiceDefinition;

@Component({
  selector: 'jhi-spec-update',
  templateUrl: './spec-update.component.html',
})
export class SpecUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];
  capabilities: ICapability[] = [];
  products: IProduct[] = [];
  sources: ISource[] = [];
  spectypes: ISpecType[] = [];
  servicedefinitions: IServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    version: [null, [Validators.required]],
    title: [],
    openApi: [null, [Validators.required]],
    tagsCsv: [],
    description: [],
    createdOn: [null, [Validators.required]],
    createdBy: [null, [Validators.required]],
    checksum: [null, [Validators.required]],
    filename: [null, [Validators.required]],
    valid: [null, [Validators.required]],
    order: [],
    parseError: [],
    externalDocs: [],
    sourcePath: [],
    sourceName: [],
    sourceUrl: [],
    sourceCreatedBy: [],
    sourceCreatedOn: [],
    sourceLastModifiedOn: [],
    sourceLastModifiedBy: [],
    portal: [null, Validators.required],
    capability: [null, Validators.required],
    product: [null, Validators.required],
    source: [],
    specType: [null, Validators.required],
    serviceDefinition: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected specService: SpecService,
    protected portalService: PortalService,
    protected capabilityService: CapabilityService,
    protected productService: ProductService,
    protected sourceService: SourceService,
    protected specTypeService: SpecTypeService,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => {
      if (!spec.id) {
        const today = moment().startOf('day');
        spec.createdOn = today;
        spec.sourceCreatedOn = today;
        spec.sourceLastModifiedOn = today;
      }

      this.updateForm(spec);

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));

      this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));

      this.sourceService.query().subscribe((res: HttpResponse<ISource[]>) => (this.sources = res.body || []));

      this.specTypeService.query().subscribe((res: HttpResponse<ISpecType[]>) => (this.spectypes = res.body || []));

      this.serviceDefinitionService
        .query()
        .subscribe((res: HttpResponse<IServiceDefinition[]>) => (this.servicedefinitions = res.body || []));
    });
  }

  updateForm(spec: ISpec): void {
    this.editForm.patchValue({
      id: spec.id,
      key: spec.key,
      name: spec.name,
      version: spec.version,
      title: spec.title,
      openApi: spec.openApi,
      tagsCsv: spec.tagsCsv,
      description: spec.description,
      createdOn: spec.createdOn ? spec.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: spec.createdBy,
      checksum: spec.checksum,
      filename: spec.filename,
      valid: spec.valid,
      order: spec.order,
      parseError: spec.parseError,
      externalDocs: spec.externalDocs,
      sourcePath: spec.sourcePath,
      sourceName: spec.sourceName,
      sourceUrl: spec.sourceUrl,
      sourceCreatedBy: spec.sourceCreatedBy,
      sourceCreatedOn: spec.sourceCreatedOn ? spec.sourceCreatedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedOn: spec.sourceLastModifiedOn ? spec.sourceLastModifiedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedBy: spec.sourceLastModifiedBy,
      portal: spec.portal,
      capability: spec.capability,
      product: spec.product,
      source: spec.source,
      specType: spec.specType,
      serviceDefinition: spec.serviceDefinition,
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
    const spec = this.createFromForm();
    if (spec.id !== undefined) {
      this.subscribeToSaveResponse(this.specService.update(spec));
    } else {
      this.subscribeToSaveResponse(this.specService.create(spec));
    }
  }

  private createFromForm(): ISpec {
    return {
      ...new Spec(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      version: this.editForm.get(['version'])!.value,
      title: this.editForm.get(['title'])!.value,
      openApi: this.editForm.get(['openApi'])!.value,
      tagsCsv: this.editForm.get(['tagsCsv'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      checksum: this.editForm.get(['checksum'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      valid: this.editForm.get(['valid'])!.value,
      order: this.editForm.get(['order'])!.value,
      parseError: this.editForm.get(['parseError'])!.value,
      externalDocs: this.editForm.get(['externalDocs'])!.value,
      sourcePath: this.editForm.get(['sourcePath'])!.value,
      sourceName: this.editForm.get(['sourceName'])!.value,
      sourceUrl: this.editForm.get(['sourceUrl'])!.value,
      sourceCreatedBy: this.editForm.get(['sourceCreatedBy'])!.value,
      sourceCreatedOn: this.editForm.get(['sourceCreatedOn'])!.value
        ? moment(this.editForm.get(['sourceCreatedOn'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sourceLastModifiedOn: this.editForm.get(['sourceLastModifiedOn'])!.value
        ? moment(this.editForm.get(['sourceLastModifiedOn'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sourceLastModifiedBy: this.editForm.get(['sourceLastModifiedBy'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      product: this.editForm.get(['product'])!.value,
      source: this.editForm.get(['source'])!.value,
      specType: this.editForm.get(['specType'])!.value,
      serviceDefinition: this.editForm.get(['serviceDefinition'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpec>>): void {
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
}
