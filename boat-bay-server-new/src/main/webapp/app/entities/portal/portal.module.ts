import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PortalComponent } from './list/portal.component';
import { PortalDetailComponent } from './detail/portal-detail.component';
import { PortalUpdateComponent } from './update/portal-update.component';
import { PortalDeleteDialogComponent } from './delete/portal-delete-dialog.component';
import { PortalRoutingModule } from './route/portal-routing.module';

@NgModule({
  imports: [SharedModule, PortalRoutingModule],
  declarations: [PortalComponent, PortalDetailComponent, PortalUpdateComponent, PortalDeleteDialogComponent],
  entryComponents: [PortalDeleteDialogComponent],
})
export class PortalModule {}
