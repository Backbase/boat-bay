import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpecType } from 'app/shared/model/spec-type.model';

@Component({
  selector: 'jhi-spec-type-detail',
  templateUrl: './spec-type-detail.component.html',
})
export class SpecTypeDetailComponent implements OnInit {
  specType: ISpecType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specType }) => (this.specType = specType));
  }

  previousState(): void {
    window.history.back();
  }
}
