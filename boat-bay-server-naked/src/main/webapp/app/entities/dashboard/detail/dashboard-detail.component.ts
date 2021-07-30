import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDashboard } from '../dashboard.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-dashboard-detail',
  templateUrl: './dashboard-detail.component.html',
})
export class DashboardDetailComponent implements OnInit {
  dashboard: IDashboard | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dashboard }) => {
      this.dashboard = dashboard;
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
