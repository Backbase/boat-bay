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
import { IService } from 'app/shared/model/service.model';
import { ServiceService } from 'app/entities/service/service.service';

type SelectableEntity = ILintReport | IService;

@Component({
  selector: 'jhi-spec-update',
  templateUrl: './spec-update.component.html',
})
export class SpecUpdateComponent implements OnInit {
  isSaving = false;
  lintreports: ILintReport[] = [];
  services: IService[] = [];

  editForm = this.fb.group({
    id: [],
    key: [],
    title: [],
    openApiUrl: [],
    boatDocUrl: [],
    openApi: [],
    createdOn: [],
    createdBy: [],
    lintReport: [],
    service: [],
  });

  constructor(
    protected specService: SpecService,
    protected lintReportService: LintReportService,
    protected serviceService: ServiceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => {
      if (!spec.id) {
        const today = moment().startOf('day');
        spec.createdOn = today;
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

      this.serviceService.query().subscribe((res: HttpResponse<IService[]>) => (this.services = res.body || []));
    });
  }

  updateForm(spec: ISpec): void {
    this.editForm.patchValue({
      id: spec.id,
      key: spec.key,
      title: spec.title,
      openApiUrl: spec.openApiUrl,
      boatDocUrl: spec.boatDocUrl,
      openApi: spec.openApi,
      createdOn: spec.createdOn ? spec.createdOn.format(DATE_TIME_FORMAT) : null,
      createdBy: spec.createdBy,
      lintReport: spec.lintReport,
      service: spec.service,
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
      title: this.editForm.get(['title'])!.value,
      openApiUrl: this.editForm.get(['openApiUrl'])!.value,
      boatDocUrl: this.editForm.get(['boatDocUrl'])!.value,
      openApi: this.editForm.get(['openApi'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lintReport: this.editForm.get(['lintReport'])!.value,
      service: this.editForm.get(['service'])!.value,
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
