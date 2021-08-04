import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISource } from '../source.model';

@Component({
  selector: 'jhi-source-detail',
  templateUrl: './source-detail.component.html',
})
export class SourceDetailComponent implements OnInit {
  source: ISource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ source }) => {
      this.source = source;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
