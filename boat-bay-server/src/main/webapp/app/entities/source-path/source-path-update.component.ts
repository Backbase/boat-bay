import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISourcePath, SourcePath } from 'app/shared/model/source-path.model';
import { SourcePathService } from './source-path.service';
import { ISource } from 'app/shared/model/source.model';
import { SourceService } from 'app/entities/source/source.service';

@Component({
  selector: 'jhi-source-path-update',
  templateUrl: './source-path-update.component.html',
})
export class SourcePathUpdateComponent implements OnInit {
  isSaving = false;
  sources: ISource[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    source: [],
  });

  constructor(
    protected sourcePathService: SourcePathService,
    protected sourceService: SourceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourcePath }) => {
      this.updateForm(sourcePath);

      this.sourceService.query().subscribe((res: HttpResponse<ISource[]>) => (this.sources = res.body || []));
    });
  }

  updateForm(sourcePath: ISourcePath): void {
    this.editForm.patchValue({
      id: sourcePath.id,
      name: sourcePath.name,
      source: sourcePath.source,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sourcePath = this.createFromForm();
    if (sourcePath.id !== undefined) {
      this.subscribeToSaveResponse(this.sourcePathService.update(sourcePath));
    } else {
      this.subscribeToSaveResponse(this.sourcePathService.create(sourcePath));
    }
  }

  private createFromForm(): ISourcePath {
    return {
      ...new SourcePath(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      source: this.editForm.get(['source'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISourcePath>>): void {
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

  trackById(index: number, item: ISource): any {
    return item.id;
  }
}
