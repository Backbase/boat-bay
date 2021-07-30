import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LintReportComponent } from './list/lint-report.component';
import { LintReportDetailComponent } from './detail/lint-report-detail.component';
import { LintReportUpdateComponent } from './update/lint-report-update.component';
import { LintReportDeleteDialogComponent } from './delete/lint-report-delete-dialog.component';
import { LintReportRoutingModule } from './route/lint-report-routing.module';

@NgModule({
  imports: [SharedModule, LintReportRoutingModule],
  declarations: [LintReportComponent, LintReportDetailComponent, LintReportUpdateComponent, LintReportDeleteDialogComponent],
  entryComponents: [LintReportDeleteDialogComponent],
})
export class LintReportModule {}
