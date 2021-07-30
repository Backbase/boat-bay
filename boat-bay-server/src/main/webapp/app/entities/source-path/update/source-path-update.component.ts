import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISourcePath, SourcePath } from '../source-path.model';
import { SourcePathService } from '../service/source-path.service';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';

@Component({
  selector: 'jhi-source-path-update',
  templateUrl: './source-path-update.component.html',
})
export class SourcePathUpdateComponent implements OnInit {
  isSaving = false;

  sourcesSharedCollection: ISource[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    source: [],
  });

  constructor(
    protected sourcePathService: SourcePathService,
    protected sourceService: SourceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourcePath }) => {
      this.updateForm(sourcePath);

      this.loadRelationshipsOptions();
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

  trackSourceById(index: number, item: ISource): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISourcePath>>): void {
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

  protected updateForm(sourcePath: ISourcePath): void {
    this.editForm.patchValue({
      id: sourcePath.id,
      name: sourcePath.name,
      source: sourcePath.source,
    });

    this.sourcesSharedCollection = this.sourceService.addSourceToCollectionIfMissing(this.sourcesSharedCollection, sourcePath.source);
  }

  protected loadRelationshipsOptions(): void {
    this.sourceService
      .query()
      .pipe(map((res: HttpResponse<ISource[]>) => res.body ?? []))
      .pipe(map((sources: ISource[]) => this.sourceService.addSourceToCollectionIfMissing(sources, this.editForm.get('source')!.value)))
      .subscribe((sources: ISource[]) => (this.sourcesSharedCollection = sources));
  }

  protected createFromForm(): ISourcePath {
    return {
      ...new SourcePath(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      source: this.editForm.get(['source'])!.value,
    };
  }
}
