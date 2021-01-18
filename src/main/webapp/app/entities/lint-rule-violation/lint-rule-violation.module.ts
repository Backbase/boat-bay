import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintRuleViolationComponent } from './lint-rule-violation.component';
import { LintRuleViolationDetailComponent } from './lint-rule-violation-detail.component';
import { LintRuleViolationUpdateComponent } from './lint-rule-violation-update.component';
import { LintRuleViolationDeleteDialogComponent } from './lint-rule-violation-delete-dialog.component';
import { lintRuleViolationRoute } from './lint-rule-violation.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintRuleViolationRoute)],
  declarations: [
    LintRuleViolationComponent,
    LintRuleViolationDetailComponent,
    LintRuleViolationUpdateComponent,
    LintRuleViolationDeleteDialogComponent,
  ],
  entryComponents: [LintRuleViolationDeleteDialogComponent],
})
export class BoatBayLintRuleViolationModule {}
