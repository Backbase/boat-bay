import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISource, Source } from '../source.model';
import { SourceService } from '../service/source.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service/service-definition.service';

@Component({
  selector: 'jhi-source-update',
  templateUrl: './source-update.component.html',
})
export class SourceUpdateComponent implements OnInit {
  isSaving = false;

  portalsSharedCollection: IPortal[] = [];
  productsSharedCollection: IProduct[] = [];
  capabilitiesSharedCollection: ICapability[] = [];
  serviceDefinitionsSharedCollection: IServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    key: [null, [Validators.required]],
    type: [null, [Validators.required]],
    baseUrl: [null, [Validators.required]],
    active: [],
    filterArtifactsName: [],
    filterArtifactsCreatedSince: [],
    username: [],
    password: [],
    cronExpression: [],
    runOnStartup: [],
    specFilterSpEL: [],
    capabilityKeySpEL: [],
    capabilityNameSpEL: [],
    serviceKeySpEL: [],
    serviceNameSpEL: [],
    specKeySpEL: [],
    versionSpEL: [],
    productReleaseNameSpEL: [],
    productReleaseVersionSpEL: [],
    productReleaseKeySpEL: [],
    itemLimit: [],
    overwriteChanges: [],
    options: [],
    portal: [null, Validators.required],
    product: [null, Validators.required],
    capability: [],
    serviceDefinition: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sourceService: SourceService,
    protected portalService: PortalService,
    protected productService: ProductService,
    protected capabilityService: CapabilityService,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ source }) => {
      this.updateForm(source);

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
    const source = this.createFromForm();
    if (source.id !== undefined) {
      this.subscribeToSaveResponse(this.sourceService.update(source));
    } else {
      this.subscribeToSaveResponse(this.sourceService.create(source));
    }
  }

  trackPortalById(index: number, item: IPortal): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackCapabilityById(index: number, item: ICapability): number {
    return item.id!;
  }

  trackServiceDefinitionById(index: number, item: IServiceDefinition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISource>>): void {
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

  protected updateForm(source: ISource): void {
    this.editForm.patchValue({
      id: source.id,
      name: source.name,
      key: source.key,
      type: source.type,
      baseUrl: source.baseUrl,
      active: source.active,
      filterArtifactsName: source.filterArtifactsName,
      filterArtifactsCreatedSince: source.filterArtifactsCreatedSince,
      username: source.username,
      password: source.password,
      cronExpression: source.cronExpression,
      runOnStartup: source.runOnStartup,
      specFilterSpEL: source.specFilterSpEL,
      capabilityKeySpEL: source.capabilityKeySpEL,
      capabilityNameSpEL: source.capabilityNameSpEL,
      serviceKeySpEL: source.serviceKeySpEL,
      serviceNameSpEL: source.serviceNameSpEL,
      specKeySpEL: source.specKeySpEL,
      versionSpEL: source.versionSpEL,
      productReleaseNameSpEL: source.productReleaseNameSpEL,
      productReleaseVersionSpEL: source.productReleaseVersionSpEL,
      productReleaseKeySpEL: source.productReleaseKeySpEL,
      itemLimit: source.itemLimit,
      overwriteChanges: source.overwriteChanges,
      options: source.options,
      portal: source.portal,
      product: source.product,
      capability: source.capability,
      serviceDefinition: source.serviceDefinition,
    });

    this.portalsSharedCollection = this.portalService.addPortalToCollectionIfMissing(this.portalsSharedCollection, source.portal);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, source.product);
    this.capabilitiesSharedCollection = this.capabilityService.addCapabilityToCollectionIfMissing(
      this.capabilitiesSharedCollection,
      source.capability
    );
    this.serviceDefinitionsSharedCollection = this.serviceDefinitionService.addServiceDefinitionToCollectionIfMissing(
      this.serviceDefinitionsSharedCollection,
      source.serviceDefinition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.portalService
      .query()
      .pipe(map((res: HttpResponse<IPortal[]>) => res.body ?? []))
      .pipe(map((portals: IPortal[]) => this.portalService.addPortalToCollectionIfMissing(portals, this.editForm.get('portal')!.value)))
      .subscribe((portals: IPortal[]) => (this.portalsSharedCollection = portals));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.capabilityService
      .query()
      .pipe(map((res: HttpResponse<ICapability[]>) => res.body ?? []))
      .pipe(
        map((capabilities: ICapability[]) =>
          this.capabilityService.addCapabilityToCollectionIfMissing(capabilities, this.editForm.get('capability')!.value)
        )
      )
      .subscribe((capabilities: ICapability[]) => (this.capabilitiesSharedCollection = capabilities));

    this.serviceDefinitionService
      .query()
      .pipe(map((res: HttpResponse<IServiceDefinition[]>) => res.body ?? []))
      .pipe(
        map((serviceDefinitions: IServiceDefinition[]) =>
          this.serviceDefinitionService.addServiceDefinitionToCollectionIfMissing(
            serviceDefinitions,
            this.editForm.get('serviceDefinition')!.value
          )
        )
      )
      .subscribe((serviceDefinitions: IServiceDefinition[]) => (this.serviceDefinitionsSharedCollection = serviceDefinitions));
  }

  protected createFromForm(): ISource {
    return {
      ...new Source(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      key: this.editForm.get(['key'])!.value,
      type: this.editForm.get(['type'])!.value,
      baseUrl: this.editForm.get(['baseUrl'])!.value,
      active: this.editForm.get(['active'])!.value,
      filterArtifactsName: this.editForm.get(['filterArtifactsName'])!.value,
      filterArtifactsCreatedSince: this.editForm.get(['filterArtifactsCreatedSince'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      cronExpression: this.editForm.get(['cronExpression'])!.value,
      runOnStartup: this.editForm.get(['runOnStartup'])!.value,
      specFilterSpEL: this.editForm.get(['specFilterSpEL'])!.value,
      capabilityKeySpEL: this.editForm.get(['capabilityKeySpEL'])!.value,
      capabilityNameSpEL: this.editForm.get(['capabilityNameSpEL'])!.value,
      serviceKeySpEL: this.editForm.get(['serviceKeySpEL'])!.value,
      serviceNameSpEL: this.editForm.get(['serviceNameSpEL'])!.value,
      specKeySpEL: this.editForm.get(['specKeySpEL'])!.value,
      versionSpEL: this.editForm.get(['versionSpEL'])!.value,
      productReleaseNameSpEL: this.editForm.get(['productReleaseNameSpEL'])!.value,
      productReleaseVersionSpEL: this.editForm.get(['productReleaseVersionSpEL'])!.value,
      productReleaseKeySpEL: this.editForm.get(['productReleaseKeySpEL'])!.value,
      itemLimit: this.editForm.get(['itemLimit'])!.value,
      overwriteChanges: this.editForm.get(['overwriteChanges'])!.value,
      options: this.editForm.get(['options'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      product: this.editForm.get(['product'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      serviceDefinition: this.editForm.get(['serviceDefinition'])!.value,
    };
  }
}
