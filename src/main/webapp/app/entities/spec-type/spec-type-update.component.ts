import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISpecType, SpecType } from 'app/shared/model/spec-type.model';
import { SpecTypeService } from './spec-type.service';

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
    matchSpEL: [null, [Validators.required]],
    icon: [null, [Validators.required]],
  });

  constructor(protected specTypeService: SpecTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specType }) => {
      this.updateForm(specType);
    });
  }

  updateForm(specType: ISpecType): void {
    this.editForm.patchValue({
      id: specType.id,
      name: specType.name,
      description: specType.description,
      matchSpEL: specType.matchSpEL,
      icon: specType.icon,
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

  private createFromForm(): ISpecType {
    return {
      ...new SpecType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      matchSpEL: this.editForm.get(['matchSpEL'])!.value,
      icon: this.editForm.get(['icon'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecType>>): void {
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
}
