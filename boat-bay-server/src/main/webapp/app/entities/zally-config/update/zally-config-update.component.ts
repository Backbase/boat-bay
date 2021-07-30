import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IZallyConfig, ZallyConfig } from '../zally-config.model';
import { ZallyConfigService } from '../service/zally-config.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';

@Component({
  selector: 'jhi-zally-config-update',
  templateUrl: './zally-config-update.component.html',
})
export class ZallyConfigUpdateComponent implements OnInit {
  isSaving = false;

  portalsCollection: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    contents: [],
    portal: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected zallyConfigService: ZallyConfigService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zallyConfig }) => {
      this.updateForm(zallyConfig);

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
    const zallyConfig = this.createFromForm();
    if (zallyConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.zallyConfigService.update(zallyConfig));
    } else {
      this.subscribeToSaveResponse(this.zallyConfigService.create(zallyConfig));
    }
  }

  trackPortalById(index: number, item: IPortal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZallyConfig>>): void {
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

  protected updateForm(zallyConfig: IZallyConfig): void {
    this.editForm.patchValue({
      id: zallyConfig.id,
      name: zallyConfig.name,
      contents: zallyConfig.contents,
      portal: zallyConfig.portal,
    });

    this.portalsCollection = this.portalService.addPortalToCollectionIfMissing(this.portalsCollection, zallyConfig.portal);
  }

  protected loadRelationshipsOptions(): void {
    this.portalService
      .query({ filter: 'zallyconfig-is-null' })
      .pipe(map((res: HttpResponse<IPortal[]>) => res.body ?? []))
      .pipe(map((portals: IPortal[]) => this.portalService.addPortalToCollectionIfMissing(portals, this.editForm.get('portal')!.value)))
      .subscribe((portals: IPortal[]) => (this.portalsCollection = portals));
  }

  protected createFromForm(): IZallyConfig {
    return {
      ...new ZallyConfig(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contents: this.editForm.get(['contents'])!.value,
      portal: this.editForm.get(['portal'])!.value,
    };
  }
}
