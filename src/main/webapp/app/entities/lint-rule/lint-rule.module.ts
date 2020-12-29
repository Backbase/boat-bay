import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { LintRuleComponent } from './lint-rule.component';
import { LintRuleDetailComponent } from './lint-rule-detail.component';
import { lintRuleRoute } from './lint-rule.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(lintRuleRoute)],
  declarations: [LintRuleComponent, LintRuleDetailComponent],
})
export class BoatBayLintRuleModule {}
