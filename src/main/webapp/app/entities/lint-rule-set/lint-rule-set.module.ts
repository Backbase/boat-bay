import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintRuleSetComponent } from './lint-rule-set.component';
import { LintRuleSetDetailComponent } from './lint-rule-set-detail.component';
import { lintRuleSetRoute } from './lint-rule-set.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintRuleSetRoute)],
  declarations: [LintRuleSetComponent, LintRuleSetDetailComponent],
})
export class BoatBayLintRuleSetModule {}
