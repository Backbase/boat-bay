import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IZallyConfig } from 'app/shared/model/zally-config.model';

@Component({
  selector: 'jhi-zally-config-detail',
  templateUrl: './zally-config-detail.component.html',
})
export class ZallyConfigDetailComponent implements OnInit {
  zallyConfig: IZallyConfig | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zallyConfig }) => (this.zallyConfig = zallyConfig));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
