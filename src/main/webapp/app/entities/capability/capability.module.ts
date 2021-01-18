import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { CapabilityComponent } from './capability.component';
import { CapabilityDetailComponent } from './capability-detail.component';
import { CapabilityUpdateComponent } from './capability-update.component';
import { CapabilityDeleteDialogComponent } from './capability-delete-dialog.component';
import { capabilityRoute } from './capability.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(capabilityRoute)],
  declarations: [CapabilityComponent, CapabilityDetailComponent, CapabilityUpdateComponent, CapabilityDeleteDialogComponent],
  entryComponents: [CapabilityDeleteDialogComponent],
})
export class BoatBayCapabilityModule {}
