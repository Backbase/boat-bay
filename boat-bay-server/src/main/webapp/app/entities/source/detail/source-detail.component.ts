import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISource } from '../source.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-source-detail',
  templateUrl: './source-detail.component.html',
})
export class SourceDetailComponent implements OnInit {
  source: ISource | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ source }) => {
      this.source = source;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
