import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISpecType, SpecType } from '../spec-type.model';
import { SpecTypeService } from '../service/spec-type.service';

@Component({
  selector: 'jhi-spec-type-update',
  templateUrl: './spec-type-update.component.html',
})
export class SpecTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    matchSpEL: [],
    icon: [null, [Validators.required]],
  });

  constructor(protected specTypeService: SpecTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specType }) => {
      this.updateForm(specType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specType = this.createFromForm();
    if (specType.id !== undefined) {
      this.subscribeToSaveResponse(this.specTypeService.update(specType));
    } else {
      this.subscribeToSaveResponse(this.specTypeService.create(specType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecType>>): void {
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

  protected updateForm(specType: ISpecType): void {
    this.editForm.patchValue({
      id: specType.id,
      name: specType.name,
      description: specType.description,
      matchSpEL: specType.matchSpEL,
      icon: specType.icon,
    });
  }

  protected createFromForm(): ISpecType {
    return {
      ...new SpecType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      matchSpEL: this.editForm.get(['matchSpEL'])!.value,
      icon: this.editForm.get(['icon'])!.value,
    };
  }
}
