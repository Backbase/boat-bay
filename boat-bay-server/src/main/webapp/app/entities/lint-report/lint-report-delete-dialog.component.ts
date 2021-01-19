import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from './lint-report.service';

@Component({
  templateUrl: './lint-report-delete-dialog.component.html',
})
export class LintReportDeleteDialogComponent {
  lintReport?: ILintReport;

  constructor(
    protected lintReportService: LintReportService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintReportService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lintReportListModification');
      this.activeModal.close();
    });
  }
}
