import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IZallyConfig, ZallyConfig } from 'app/shared/model/zally-config.model';
import { ZallyConfigService } from './zally-config.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

@Component({
  selector: 'jhi-zally-config-update',
  templateUrl: './zally-config-update.component.html',
})
export class ZallyConfigUpdateComponent implements OnInit {
  isSaving = false;
  portals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    contents: [],
    portal: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected zallyConfigService: ZallyConfigService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zallyConfig }) => {
      this.updateForm(zallyConfig);

      this.portalService
        .query({ filter: 'zallyconfig-is-null' })
        .pipe(
          map((res: HttpResponse<IPortal[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPortal[]) => {
          if (!zallyConfig.portal || !zallyConfig.portal.id) {
            this.portals = resBody;
          } else {
            this.portalService
              .find(zallyConfig.portal.id)
              .pipe(
                map((subRes: HttpResponse<IPortal>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPortal[]) => (this.portals = concatRes));
          }
        });
    });
  }

  updateForm(zallyConfig: IZallyConfig): void {
    this.editForm.patchValue({
      id: zallyConfig.id,
      name: zallyConfig.name,
      contents: zallyConfig.contents,
      portal: zallyConfig.portal,
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
    const zallyConfig = this.createFromForm();
    if (zallyConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.zallyConfigService.update(zallyConfig));
    } else {
      this.subscribeToSaveResponse(this.zallyConfigService.create(zallyConfig));
    }
  }

  private createFromForm(): IZallyConfig {
    return {
      ...new ZallyConfig(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contents: this.editForm.get(['contents'])!.value,
      portal: this.editForm.get(['portal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZallyConfig>>): void {
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

  trackById(index: number, item: IPortal): any {
    return item.id;
  }
}
