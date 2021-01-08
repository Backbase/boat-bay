import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AceEditorComponent } from '../ace-editor/ace-editor.component';
import { LintReportComponent } from './lint-report.component';
import { LintReportListComponent } from '../lint-report-list/lint-report-list.component';

@NgModule({
  declarations: [AceEditorComponent, LintReportComponent, LintReportListComponent],
  imports: [CommonModule],
})
export class LintReportModule {}
