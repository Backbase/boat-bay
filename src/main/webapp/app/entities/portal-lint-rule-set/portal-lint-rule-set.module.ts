import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { PortalLintRuleSetComponent } from './portal-lint-rule-set.component';
import { PortalLintRuleSetDetailComponent } from './portal-lint-rule-set-detail.component';
import { PortalLintRuleSetUpdateComponent } from './portal-lint-rule-set-update.component';
import { PortalLintRuleSetDeleteDialogComponent } from './portal-lint-rule-set-delete-dialog.component';
import { portalLintRuleSetRoute } from './portal-lint-rule-set.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(portalLintRuleSetRoute)],
  declarations: [
    PortalLintRuleSetComponent,
    PortalLintRuleSetDetailComponent,
    PortalLintRuleSetUpdateComponent,
    PortalLintRuleSetDeleteDialogComponent,
  ],
  entryComponents: [PortalLintRuleSetDeleteDialogComponent],
})
export class BoatBayPortalLintRuleSetModule {}
