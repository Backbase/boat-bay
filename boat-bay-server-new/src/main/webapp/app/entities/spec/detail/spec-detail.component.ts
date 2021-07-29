import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpec } from '../spec.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-spec-detail',
  templateUrl: './spec-detail.component.html',
})
export class SpecDetailComponent implements OnInit {
  spec: ISpec | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spec }) => {
      this.spec = spec;
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
