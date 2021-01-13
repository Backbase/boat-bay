import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintRuleComponent } from './lint-rule.component';
import { LintRuleDetailComponent } from './lint-rule-detail.component';
import { LintRuleUpdateComponent } from './lint-rule-update.component';
import { LintRuleDeleteDialogComponent } from './lint-rule-delete-dialog.component';
import { lintRuleRoute } from './lint-rule.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintRuleRoute)],
  declarations: [LintRuleComponent, LintRuleDetailComponent, LintRuleUpdateComponent, LintRuleDeleteDialogComponent],
  entryComponents: [LintRuleDeleteDialogComponent],
})
export class BoatBayLintRuleModule {}
