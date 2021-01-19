import { NgModule } from '@angular/core';
import { LintReportComponent } from "./lint-report.component";
import { AceEditorComponent } from "../../components/ace-editor/ace-editor.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { CommonModule } from "@angular/common";
import { MatTabsModule } from "@angular/material/tabs";
import { CreateJiraIssueButtonComponent } from "../../components/create-jira-issue-button/create-jira-issue-button.component";


@NgModule({
  declarations: [
    LintReportComponent,
    AceEditorComponent,
    CreateJiraIssueButtonComponent
  ],
  exports: [
    LintReportComponent
  ],
  imports: [
    MatCardModule,
    MatButtonModule,
    CommonModule,
    MatTabsModule

  ]
})
export class LintReportModule { }
