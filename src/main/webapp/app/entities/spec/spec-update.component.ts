import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISpec, Spec } from 'app/shared/model/spec.model';
import { SpecService } from './spec.service';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from 'app/entities/lint-report/lint-report.service';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';
import { ICapability } from 'app/shared/model/capability.model';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from 'app/entities/capability-service-definition/capability-service-definition.service';
import { ISource } from 'app/shared/model/source.model';
import { SourceService } from 'app/entities/source/source.service';

type SelectableEntity = ILintReport | IPortal | ICapability | ICapabilityServiceDefinition | ISource;

@Component({
  selector: 'jhi-spec-update',
  templateUrl: './spec-update.component.html',
})
export class SpecUpdateComponent implements OnInit {
  isSaving = false;
  lintreports: ILintReport[] = [];
  portals: IPortal[] = [];
  capabilities: ICapability[] = [];
  capabilityservicedefinitions: ICapabilityServiceDefinition[] = [];
  sources: ISource[] = [];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    version: [null, [Validators.required]],
    title: [],
    openApi: [null, [Validators.required]],
    createdOn: [null, [Validators.required]],
    createdBy: [null, [Validators.required]],
    checksum: [null, [Validators.required]],
    filename: [null, [Validators.required]],
    sourcePath: [],
    sourceName: [],
    sourceUrl: [],
    sourceCreatedBy: [],
    sourceCreatedOn: [],
    sourceLastModifiedOn: [],
    sourceLastModifiedBy: [],
    lintReport: [],
    portal: [null, Validators.required],
    capability: [null, Validators.required],
    capabilityServiceDefinition: [null, Validators.required],
    source: [],
  });

  constructor(
    protected specService: SpecService,
    protected lintReportService: LintReportService,
    protected portalService: PortalService,
    protected capabilityService: CapabilityService,
    protected capabilityServiceDefinitionService: CapabilityServiceDefinitionService,
    protected sourceService: SourceService,
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

      this.lintReportService
        .query({ filter: 'spec-is-null' })
        .pipe(
          map((res: HttpResponse<ILintReport[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ILintReport[]) => {
          if (!spec.lintReport || !spec.lintReport.id) {
            this.lintreports = resBody;
          } else {
            this.lintReportService
              .find(spec.lintReport.id)
              .pipe(
                map((subRes: HttpResponse<ILintReport>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILintReport[]) => (this.lintreports = concatRes));
          }
        });

      this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));

      this.capabilityService.query().subscribe((res: HttpResponse<ICapability[]>) => (this.capabilities = res.body || []));

      this.capabilityServiceDefinitionService
        .query()
        .subscribe((res: HttpResponse<ICapabilityServiceDefinition[]>) => (this.capabilityservicedefinitions = res.body || []));

      this.sourceService.query().subscribe((res: HttpResponse<ISource[]>) => (this.sources = res.body || []));
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
      createdOn: spec.createdOn ? spec.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: spec.createdBy,
      checksum: spec.checksum,
      filename: spec.filename,
      sourcePath: spec.sourcePath,
      sourceName: spec.sourceName,
      sourceUrl: spec.sourceUrl,
      sourceCreatedBy: spec.sourceCreatedBy,
      sourceCreatedOn: spec.sourceCreatedOn ? spec.sourceCreatedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedOn: spec.sourceLastModifiedOn ? spec.sourceLastModifiedOn.format(DATE_TIME_FORMAT) : null,
      sourceLastModifiedBy: spec.sourceLastModifiedBy,
      lintReport: spec.lintReport,
      portal: spec.portal,
      capability: spec.capability,
      capabilityServiceDefinition: spec.capabilityServiceDefinition,
      source: spec.source,
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
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      checksum: this.editForm.get(['checksum'])!.value,
      filename: this.editForm.get(['filename'])!.value,
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
      lintReport: this.editForm.get(['lintReport'])!.value,
      portal: this.editForm.get(['portal'])!.value,
      capability: this.editForm.get(['capability'])!.value,
      capabilityServiceDefinition: this.editForm.get(['capabilityServiceDefinition'])!.value,
      source: this.editForm.get(['source'])!.value,
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
