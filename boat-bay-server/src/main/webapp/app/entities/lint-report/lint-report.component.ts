import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from './lint-report.service';
import { LintReportDeleteDialogComponent } from './lint-report-delete-dialog.component';

@Component({
  selector: 'jhi-lint-report',
  templateUrl: './lint-report.component.html',
})
export class LintReportComponent implements OnInit, OnDestroy {
  lintReports?: ILintReport[];
  eventSubscriber?: Subscription;

  constructor(protected lintReportService: LintReportService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.lintReportService.query().subscribe((res: HttpResponse<ILintReport[]>) => (this.lintReports = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLintReports();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILintReport): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLintReports(): void {
    this.eventSubscriber = this.eventManager.subscribe('lintReportListModification', () => this.loadAll());
  }

  delete(lintReport: ILintReport): void {
    const modalRef = this.modalService.open(LintReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lintReport = lintReport;
  }
}
