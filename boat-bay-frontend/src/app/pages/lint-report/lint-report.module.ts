import { NgModule } from '@angular/core';
import { LintReportComponent } from "./lint-report.component";
import { AceEditorComponent } from "../../components/ace-editor/ace-editor.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { CommonModule } from "@angular/common";
import { CreateJiraIssueButtonComponent } from "../../components/create-jira-issue-button/create-jira-issue-button.component";
import { SpecSummaryComponent } from "../../components/spec-summary/spec-summary.component";
import { MatListModule } from "@angular/material/list";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { RouterModule } from "@angular/router";
import { DisableRuleModalDialogComponent } from "../../components/disable-rule-modal-dialog/disable-rule-modal-dialog.component";
import { MatDialogModule } from "@angular/material/dialog";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";


@NgModule({
  declarations: [
    LintReportComponent,
    AceEditorComponent,
    CreateJiraIssueButtonComponent,
    SpecSummaryComponent,
    DisableRuleModalDialogComponent

  ],
  providers: [
    MatSnackBar
  ],
  exports: [
    LintReportComponent,
    SpecSummaryComponent
  ],
  imports: [
    MatCardModule,
    MatButtonModule,
    CommonModule,
    MatListModule,
    MatCheckboxModule,
    RouterModule,
    MatDialogModule,
    MatSnackBarModule
  ]

})
export class LintReportModule {
}
