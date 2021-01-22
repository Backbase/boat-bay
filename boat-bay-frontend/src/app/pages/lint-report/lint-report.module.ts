import { NgModule } from '@angular/core';
import { LintReportComponent } from "./lint-report.component";
import { AceEditorComponent } from "../../components/ace-editor/ace-editor.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { CommonModule } from "@angular/common";
import { MatTabsModule } from "@angular/material/tabs";
import { CreateJiraIssueButtonComponent } from "../../components/create-jira-issue-button/create-jira-issue-button.component";
import { SpecSummaryComponent } from "../../components/spec-summary/spec-summary.component";
import { MatListModule } from "@angular/material/list";
import { MatCheckboxModule } from "@angular/material/checkbox";


@NgModule({
  declarations: [
    LintReportComponent,
    AceEditorComponent,
    CreateJiraIssueButtonComponent,
    SpecSummaryComponent
  ],
    exports: [
        LintReportComponent,
        SpecSummaryComponent
    ],
  imports: [
    MatCardModule,
    MatButtonModule,
    CommonModule,
    MatTabsModule,
    MatListModule,
    MatCheckboxModule
  ]
})
export class LintReportModule {
}
