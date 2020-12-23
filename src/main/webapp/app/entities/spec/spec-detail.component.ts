import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpec } from 'app/shared/model/spec.model';

@Component({
  selector: 'jhi-spec-detail',
  templateUrl: './spec-detail.component.html',
})
export class SpecDetailComponent implements OnInit {
  spec: ISpec | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => (this.spec = spec));
  }

  previousState(): void {
    window.history.back();
  }
}
