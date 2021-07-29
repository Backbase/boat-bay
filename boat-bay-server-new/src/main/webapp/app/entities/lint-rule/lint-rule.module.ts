import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LintRuleComponent } from './list/lint-rule.component';
import { LintRuleDetailComponent } from './detail/lint-rule-detail.component';
import { LintRuleUpdateComponent } from './update/lint-rule-update.component';
import { LintRuleDeleteDialogComponent } from './delete/lint-rule-delete-dialog.component';
import { LintRuleRoutingModule } from './route/lint-rule-routing.module';

@NgModule({
  imports: [SharedModule, LintRuleRoutingModule],
  declarations: [LintRuleComponent, LintRuleDetailComponent, LintRuleUpdateComponent, LintRuleDeleteDialogComponent],
  entryComponents: [LintRuleDeleteDialogComponent],
})
export class LintRuleModule {}
