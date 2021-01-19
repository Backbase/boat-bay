import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDashboard, Dashboard } from 'app/shared/model/dashboard.model';
import { DashboardService } from './dashboard.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPortal } from 'app/shared/model/portal.model';
import { PortalService } from 'app/entities/portal/portal.service';

@Component({
  selector: 'jhi-dashboard-update',
  templateUrl: './dashboard-update.component.html',
})
export class DashboardUpdateComponent implements OnInit {
  isSaving = false;
  defaultportals: IPortal[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    title: [],
    subTitle: [],
    navTitle: [],
    content: [],
    defaultPortal: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected dashboardService: DashboardService,
    protected portalService: PortalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dashboard }) => {
      this.updateForm(dashboard);

      this.portalService
        .query({ filter: 'dashboard-is-null' })
        .pipe(
          map((res: HttpResponse<IPortal[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPortal[]) => {
          if (!dashboard.defaultPortal || !dashboard.defaultPortal.id) {
            this.defaultportals = resBody;
          } else {
            this.portalService
              .find(dashboard.defaultPortal.id)
              .pipe(
                map((subRes: HttpResponse<IPortal>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPortal[]) => (this.defaultportals = concatRes));
          }
        });
    });
  }

  updateForm(dashboard: IDashboard): void {
    this.editForm.patchValue({
      id: dashboard.id,
      name: dashboard.name,
      title: dashboard.title,
      subTitle: dashboard.subTitle,
      navTitle: dashboard.navTitle,
      content: dashboard.content,
      defaultPortal: dashboard.defaultPortal,
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
    const dashboard = this.createFromForm();
    if (dashboard.id !== undefined) {
      this.subscribeToSaveResponse(this.dashboardService.update(dashboard));
    } else {
      this.subscribeToSaveResponse(this.dashboardService.create(dashboard));
    }
  }

  private createFromForm(): IDashboard {
    return {
      ...new Dashboard(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      navTitle: this.editForm.get(['navTitle'])!.value,
      content: this.editForm.get(['content'])!.value,
      defaultPortal: this.editForm.get(['defaultPortal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDashboard>>): void {
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
