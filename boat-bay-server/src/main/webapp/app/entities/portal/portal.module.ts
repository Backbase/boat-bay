import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { PortalComponent } from './portal.component';
import { PortalDetailComponent } from './portal-detail.component';
import { PortalUpdateComponent } from './portal-update.component';
import { PortalDeleteDialogComponent } from './portal-delete-dialog.component';
import { portalRoute } from './portal.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(portalRoute)],
  declarations: [PortalComponent, PortalDetailComponent, PortalUpdateComponent, PortalDeleteDialogComponent],
  entryComponents: [PortalDeleteDialogComponent],
})
export class BoatBayPortalModule {}
