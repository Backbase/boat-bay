import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDashboard } from 'app/shared/model/dashboard.model';
import { DashboardService } from './dashboard.service';
import { DashboardDeleteDialogComponent } from './dashboard-delete-dialog.component';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit, OnDestroy {
  dashboards?: IDashboard[];
  eventSubscriber?: Subscription;

  constructor(
    protected dashboardService: DashboardService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.dashboardService.query().subscribe((res: HttpResponse<IDashboard[]>) => (this.dashboards = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDashboards();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDashboard): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInDashboards(): void {
    this.eventSubscriber = this.eventManager.subscribe('dashboardListModification', () => this.loadAll());
  }

  delete(dashboard: IDashboard): void {
    const modalRef = this.modalService.open(DashboardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dashboard = dashboard;
  }
}
