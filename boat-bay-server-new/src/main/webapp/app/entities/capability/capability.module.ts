import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CapabilityComponent } from './list/capability.component';
import { CapabilityDetailComponent } from './detail/capability-detail.component';
import { CapabilityUpdateComponent } from './update/capability-update.component';
import { CapabilityDeleteDialogComponent } from './delete/capability-delete-dialog.component';
import { CapabilityRoutingModule } from './route/capability-routing.module';

@NgModule({
  imports: [SharedModule, CapabilityRoutingModule],
  declarations: [CapabilityComponent, CapabilityDetailComponent, CapabilityUpdateComponent, CapabilityDeleteDialogComponent],
  entryComponents: [CapabilityDeleteDialogComponent],
})
export class CapabilityModule {}
