import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISource, Source } from 'app/shared/model/source.model';
import { SourceService } from './source.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service-definition.service';

type SelectableEntity = IPortal | IProduct | ICapability | IServiceDefinition;

@Component({
  selector: 'jhi-source-update',
  templateUrl: './source-update.component.html',
})
export class SourceUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];
  products: IProduct[] = [];
  capabilities: ICapability[] = [];
  servicedefinitions: IServiceDefinition[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    type: [null, [Validators.required]],
    baseUrl: [null, [Validators.required]],
    active: [],
    filter: [],
    username: [],
    password: [],
    cronExpression: [],
    specFilterSpEL: [],
    capabilityKeySpEL: [],
    capabilityNameSpEL: [],
    serviceKeySpEL: [],
    serviceNameSpEL: [],
    versionSpEL: [],
    overwriteChanges: [],
    portal: [null, Validators.required],
    product: [null, Validators.required],
    capability: [],
    serviceDefinition: [],
  });

  constructor(
    protected sourceService: SourceService,
    protected portalService: PortalService,
    protected productService: ProductService,
    protected capabilityService: CapabilityService,
    protected serviceDefinitionService: ServiceDefinitionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ source }) => {
      this.updateForm(source);

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));

      this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));

      this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));

      this.serviceDefinitionService
        .query()
        .subscribe((res: HttpResponse<IServiceDefinition[]>) => (this.servicedefinitions = res.body || []));
    });
  }

  updateForm(source: ISource): void {
    this.editForm.patchValue({
      id: source.id,
      name: source.name,
      type: source.type,
      baseUrl: source.baseUrl,
      active: source.active,
      filter: source.filter,
      username: source.username,
      password: source.password,
      cronExpression: source.cronExpression,
      specFilterSpEL: source.specFilterSpEL,
      capabilityKeySpEL: source.capabilityKeySpEL,
      capabilityNameSpEL: source.capabilityNameSpEL,
      serviceKeySpEL: source.serviceKeySpEL,
      serviceNameSpEL: source.serviceNameSpEL,
      versionSpEL: source.versionSpEL,
      overwriteChanges: source.overwriteChanges,
      portal: source.portal,
      product: source.product,
      capability: source.capability,
      serviceDefinition: source.serviceDefinition,
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

  private createFromForm(): ISource {
    return {
      ...new Source(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      baseUrl: this.editForm.get(['baseUrl'])!.value,
      active: this.editForm.get(['active'])!.value,
      filter: this.editForm.get(['filter'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      cronExpression: this.editForm.get(['cronExpression'])!.value,
      specFilterSpEL: this.editForm.get(['specFilterSpEL'])!.value,
      capabilityKeySpEL: this.editForm.get(['capabilityKeySpEL'])!.value,
      capabilityNameSpEL: this.editForm.get(['capabilityNameSpEL'])!.value,
      serviceKeySpEL: this.editForm.get(['serviceKeySpEL'])!.value,
      serviceNameSpEL: this.editForm.get(['serviceNameSpEL'])!.value,
      versionSpEL: this.editForm.get(['versionSpEL'])!.value,
      overwriteChanges: this.editForm.get(['overwriteChanges'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      product: this.editForm.get(['product'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      serviceDefinition: this.editForm.get(['serviceDefinition'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISource>>): void {
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
