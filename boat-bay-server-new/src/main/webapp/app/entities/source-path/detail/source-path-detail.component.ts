import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISourcePath } from '../source-path.model';

@Component({
  selector: 'jhi-source-path-detail',
  templateUrl: './source-path-detail.component.html',
})
export class SourcePathDetailComponent implements OnInit {
  sourcePath: ISourcePath | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourcePath }) => {
      this.sourcePath = sourcePath;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
