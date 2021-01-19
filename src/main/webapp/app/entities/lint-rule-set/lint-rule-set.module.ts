import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintRuleSetComponent } from './lint-rule-set.component';
import { LintRuleSetDetailComponent } from './lint-rule-set-detail.component';
import { LintRuleSetUpdateComponent } from './lint-rule-set-update.component';
import { LintRuleSetDeleteDialogComponent } from './lint-rule-set-delete-dialog.component';
import { lintRuleSetRoute } from './lint-rule-set.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintRuleSetRoute)],
  declarations: [LintRuleSetComponent, LintRuleSetDetailComponent, LintRuleSetUpdateComponent, LintRuleSetDeleteDialogComponent],
  entryComponents: [LintRuleSetDeleteDialogComponent],
})
export class BoatBayLintRuleSetModule {}
