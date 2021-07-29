import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LintRuleViolationComponent } from './list/lint-rule-violation.component';
import { LintRuleViolationDetailComponent } from './detail/lint-rule-violation-detail.component';
import { LintRuleViolationUpdateComponent } from './update/lint-rule-violation-update.component';
import { LintRuleViolationDeleteDialogComponent } from './delete/lint-rule-violation-delete-dialog.component';
import { LintRuleViolationRoutingModule } from './route/lint-rule-violation-routing.module';

@NgModule({
  imports: [SharedModule, LintRuleViolationRoutingModule],
  declarations: [
    LintRuleViolationComponent,
    LintRuleViolationDetailComponent,
    LintRuleViolationUpdateComponent,
    LintRuleViolationDeleteDialogComponent,
  ],
  entryComponents: [LintRuleViolationDeleteDialogComponent],
})
export class LintRuleViolationModule {}
