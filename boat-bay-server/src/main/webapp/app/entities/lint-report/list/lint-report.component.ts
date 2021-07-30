import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintReport } from '../lint-report.model';
import { LintReportService } from '../service/lint-report.service';
import { LintReportDeleteDialogComponent } from '../delete/lint-report-delete-dialog.component';

@Component({
  selector: 'jhi-lint-report',
  templateUrl: './lint-report.component.html',
})
export class LintReportComponent implements OnInit {
  lintReports?: ILintReport[];
  isLoading = false;

  constructor(protected lintReportService: LintReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lintReportService.query().subscribe(
      (res: HttpResponse<ILintReport[]>) => {
        this.isLoading = false;
        this.lintReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILintReport): number {
    return item.id!;
  }

  delete(lintReport: ILintReport): void {
    const modalRef = this.modalService.open(LintReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lintReport = lintReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
