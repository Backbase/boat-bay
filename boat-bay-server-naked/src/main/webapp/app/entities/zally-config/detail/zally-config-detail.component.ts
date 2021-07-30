import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IZallyConfig } from '../zally-config.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-zally-config-detail',
  templateUrl: './zally-config-detail.component.html',
})
export class ZallyConfigDetailComponent implements OnInit {
  zallyConfig: IZallyConfig | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zallyConfig }) => {
      this.zallyConfig = zallyConfig;
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
