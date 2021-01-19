import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { PortalLintRuleComponent } from './portal-lint-rule.component';
import { PortalLintRuleDetailComponent } from './portal-lint-rule-detail.component';
import { PortalLintRuleUpdateComponent } from './portal-lint-rule-update.component';
import { PortalLintRuleDeleteDialogComponent } from './portal-lint-rule-delete-dialog.component';
import { portalLintRuleRoute } from './portal-lint-rule.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(portalLintRuleRoute)],
  declarations: [
    PortalLintRuleComponent,
    PortalLintRuleDetailComponent,
    PortalLintRuleUpdateComponent,
    PortalLintRuleDeleteDialogComponent,
  ],
  entryComponents: [PortalLintRuleDeleteDialogComponent],
})
export class BoatBayPortalLintRuleModule {}
