import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintReportComponent } from './lint-report.component';
import { LintReportDetailComponent } from './lint-report-detail.component';
import { LintReportUpdateComponent } from './lint-report-update.component';
import { LintReportDeleteDialogComponent } from './lint-report-delete-dialog.component';
import { lintReportRoute } from './lint-report.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintReportRoute)],
  declarations: [LintReportComponent, LintReportDetailComponent, LintReportUpdateComponent, LintReportDeleteDialogComponent],
  entryComponents: [LintReportDeleteDialogComponent],
})
export class BoatBayLintReportModule {}
