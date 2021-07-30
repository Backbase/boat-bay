import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintReport } from '../lint-report.model';
import { LintReportService } from '../service/lint-report.service';

@Component({
  templateUrl: './lint-report-delete-dialog.component.html',
})
export class LintReportDeleteDialogComponent {
  lintReport?: ILintReport;

  constructor(protected lintReportService: LintReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
