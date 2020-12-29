import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { PortalLintRuleConfigComponent } from './portal-lint-rule-config.component';
import { PortalLintRuleConfigDetailComponent } from './portal-lint-rule-config-detail.component';
import { PortalLintRuleConfigUpdateComponent } from './portal-lint-rule-config-update.component';
import { PortalLintRuleConfigDeleteDialogComponent } from './portal-lint-rule-config-delete-dialog.component';
import { portalLintRuleConfigRoute } from './portal-lint-rule-config.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(portalLintRuleConfigRoute)],
  declarations: [
    PortalLintRuleConfigComponent,
    PortalLintRuleConfigDetailComponent,
    PortalLintRuleConfigUpdateComponent,
    PortalLintRuleConfigDeleteDialogComponent,
  ],
  entryComponents: [PortalLintRuleConfigDeleteDialogComponent],
})
export class BoatBayPortalLintRuleConfigModule {}
