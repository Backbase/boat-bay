import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISpec, Spec } from '../spec.model';
import { SpecService } from '../service/spec.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { ISpecType } from 'app/entities/spec-type/spec-type.model';
import { SpecTypeService } from 'app/entities/spec-type/service/spec-type.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service/service-definition.service';

@Component({
  selector: 'jhi-spec-update',
  templateUrl: './spec-update.component.html',
})
export class SpecUpdateComponent implements OnInit {
  isSaving = false;

  previousSpecsCollection: ISpec[] = [];
  portalsSharedCollection: IPortal[] = [];
  capabilitiesSharedCollection: ICapability[] = [];
  productsSharedCollection: IProduct[] = [];
  sourcesSharedCollection: ISource[] = [];
  specTypesSharedCollection: ISpecType[] = [];
  tagsSharedCollection: ITag[] = [];
  serviceDefinitionsSharedCollection: IServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    version: [null, [Validators.required]],
    title: [],
    icon: [],
    openApi: [null, [Validators.required]],
    description: [],
    createdOn: [null, [Validators.required]],
    createdBy: [null, [Validators.required]],
    checksum: [null, [Validators.required]],
    filename: [null, [Validators.required]],
    valid: [null, [Validators.required]],
    order: [],
    parseError: [],
    externalDocs: [],
    hide: [],
    grade: [],
    changes: [],
    sourcePath: [],
    sourceName: [],
    sourceUrl: [],
    sourceCreatedBy: [],
    sourceCreatedOn: [],
    sourceLastModifiedOn: [],
    sourceLastModifiedBy: [],
    mvnGroupId: [],
    mvnArtifactId: [],
    mvnVersion: [],
    mvnClassifier: [],
    mvnExtension: [],
    previousSpec: [],
    portal: [null, Validators.required],
    capability: [],
    product: [null, Validators.required],
    source: [],
    specType: [],
    tags: [],
    serviceDefinition: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected specService: SpecService,
    protected portalService: PortalService,
    protected capabilityService: CapabilityService,
    protected productService: ProductService,
    protected sourceService: SourceService,
    protected specTypeService: SpecTypeService,
    protected tagService: TagService,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => {
      if (spec.id === undefined) {
        const today = dayjs().startOf('day');
        spec.createdOn = today;
        spec.sourceCreatedOn = today;
        spec.sourceLastModifiedOn = today;
      }

      this.updateForm(spec);

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
    const spec = this.createFromForm();
    if (spec.id !== undefined) {
      this.subscribeToSaveResponse(this.specService.update(spec));
    } else {
      this.subscribeToSaveResponse(this.specService.create(spec));
    }
  }

  trackSpecById(index: number, item: ISpec): number {
    return item.id!;
  }

  trackPortalById(index: number, item: IPortal): number {
    return item.id!;
  }

  trackCapabilityById(index: number, item: ICapability): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackSourceById(index: number, item: ISource): number {
    return item.id!;
  }

  trackSpecTypeById(index: number, item: ISpecType): number {
    return item.id!;
  }

  trackTagById(index: number, item: ITag): number {
    return item.id!;
  }

  trackServiceDefinitionById(index: number, item: IServiceDefinition): number {
    return item.id!;
  }

  getSelectedTag(option: ITag, selectedVals?: ITag[]): ITag {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpec>>): void {
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

  protected updateForm(spec: ISpec): void {
    this.editForm.patchValue({
      id: spec.id,
      key: spec.key,
      name: spec.name,
      version: spec.version,
      title: spec.title,
      icon: spec.icon,
      openApi: spec.openApi,
      description: spec.description,
      createdOn: spec.createdOn ? spec.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: spec.createdBy,
      checksum: spec.checksum,
      filename: spec.filename,
      valid: spec.valid,
      order: spec.order,
      parseError: spec.parseError,
      externalDocs: spec.externalDocs,
      hide: spec.hide,
      grade: spec.grade,
      changes: spec.changes,
      sourcePath: spec.sourcePath,
      sourceName: spec.sourceName,
      sourceUrl: spec.sourceUrl,
      sourceCreatedBy: spec.sourceCreatedBy,
      sourceCreatedOn: spec.sourceCreatedOn ? spec.sourceCreatedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedOn: spec.sourceLastModifiedOn ? spec.sourceLastModifiedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedBy: spec.sourceLastModifiedBy,
      mvnGroupId: spec.mvnGroupId,
      mvnArtifactId: spec.mvnArtifactId,
      mvnVersion: spec.mvnVersion,
      mvnClassifier: spec.mvnClassifier,
      mvnExtension: spec.mvnExtension,
      previousSpec: spec.previousSpec,
      portal: spec.portal,
      capability: spec.capability,
      product: spec.product,
      source: spec.source,
      specType: spec.specType,
      tags: spec.tags,
      serviceDefinition: spec.serviceDefinition,
    });

    this.previousSpecsCollection = this.specService.addSpecToCollectionIfMissing(this.previousSpecsCollection, spec.previousSpec);
    this.portalsSharedCollection = this.portalService.addPortalToCollectionIfMissing(this.portalsSharedCollection, spec.portal);
    this.capabilitiesSharedCollection = this.capabilityService.addCapabilityToCollectionIfMissing(
      this.capabilitiesSharedCollection,
      spec.capability
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, spec.product);
    this.sourcesSharedCollection = this.sourceService.addSourceToCollectionIfMissing(this.sourcesSharedCollection, spec.source);
    this.specTypesSharedCollection = this.specTypeService.addSpecTypeToCollectionIfMissing(this.specTypesSharedCollection, spec.specType);
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing(this.tagsSharedCollection, ...(spec.tags ?? []));
    this.serviceDefinitionsSharedCollection = this.serviceDefinitionService.addServiceDefinitionToCollectionIfMissing(
      this.serviceDefinitionsSharedCollection,
      spec.serviceDefinition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.specService
      .query({ filter: 'successor-is-null' })
      .pipe(map((res: HttpResponse<ISpec[]>) => res.body ?? []))
      .pipe(map((specs: ISpec[]) => this.specService.addSpecToCollectionIfMissing(specs, this.editForm.get('previousSpec')!.value)))
      .subscribe((specs: ISpec[]) => (this.previousSpecsCollection = specs));

    this.portalService
      .query()
      .pipe(map((res: HttpResponse<IPortal[]>) => res.body ?? []))
      .pipe(map((portals: IPortal[]) => this.portalService.addPortalToCollectionIfMissing(portals, this.editForm.get('portal')!.value)))
      .subscribe((portals: IPortal[]) => (this.portalsSharedCollection = portals));

    this.capabilityService
      .query()
      .pipe(map((res: HttpResponse<ICapability[]>) => res.body ?? []))
      .pipe(
        map((capabilities: ICapability[]) =>
          this.capabilityService.addCapabilityToCollectionIfMissing(capabilities, this.editForm.get('capability')!.value)
        )
      )
      .subscribe((capabilities: ICapability[]) => (this.capabilitiesSharedCollection = capabilities));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.sourceService
      .query()
      .pipe(map((res: HttpResponse<ISource[]>) => res.body ?? []))
      .pipe(map((sources: ISource[]) => this.sourceService.addSourceToCollectionIfMissing(sources, this.editForm.get('source')!.value)))
      .subscribe((sources: ISource[]) => (this.sourcesSharedCollection = sources));

    this.specTypeService
      .query()
      .pipe(map((res: HttpResponse<ISpecType[]>) => res.body ?? []))
      .pipe(
        map((specTypes: ISpecType[]) =>
          this.specTypeService.addSpecTypeToCollectionIfMissing(specTypes, this.editForm.get('specType')!.value)
        )
      )
      .subscribe((specTypes: ISpecType[]) => (this.specTypesSharedCollection = specTypes));

    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing(tags, ...(this.editForm.get('tags')!.value ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));

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

  protected createFromForm(): ISpec {
    return {
      ...new Spec(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      name: this.editForm.get(['name'])!.value,
      version: this.editForm.get(['version'])!.value,
      title: this.editForm.get(['title'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      openApi: this.editForm.get(['openApi'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? dayjs(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      checksum: this.editForm.get(['checksum'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      valid: this.editForm.get(['valid'])!.value,
      order: this.editForm.get(['order'])!.value,
      parseError: this.editForm.get(['parseError'])!.value,
      externalDocs: this.editForm.get(['externalDocs'])!.value,
      hide: this.editForm.get(['hide'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      changes: this.editForm.get(['changes'])!.value,
      sourcePath: this.editForm.get(['sourcePath'])!.value,
      sourceName: this.editForm.get(['sourceName'])!.value,
      sourceUrl: this.editForm.get(['sourceUrl'])!.value,
      sourceCreatedBy: this.editForm.get(['sourceCreatedBy'])!.value,
      sourceCreatedOn: this.editForm.get(['sourceCreatedOn'])!.value
        ? dayjs(this.editForm.get(['sourceCreatedOn'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sourceLastModifiedOn: this.editForm.get(['sourceLastModifiedOn'])!.value
        ? dayjs(this.editForm.get(['sourceLastModifiedOn'])!.value, DATE_TIME_FORMAT)
        : undefined,
      sourceLastModifiedBy: this.editForm.get(['sourceLastModifiedBy'])!.value,
      mvnGroupId: this.editForm.get(['mvnGroupId'])!.value,
      mvnArtifactId: this.editForm.get(['mvnArtifactId'])!.value,
      mvnVersion: this.editForm.get(['mvnVersion'])!.value,
      mvnClassifier: this.editForm.get(['mvnClassifier'])!.value,
      mvnExtension: this.editForm.get(['mvnExtension'])!.value,
      previousSpec: this.editForm.get(['previousSpec'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      product: this.editForm.get(['product'])!.value,
      source: this.editForm.get(['source'])!.value,
      specType: this.editForm.get(['specType'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      serviceDefinition: this.editForm.get(['serviceDefinition'])!.value,
    };
  }
}
