import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { CapabilityServiceDefinitionComponent } from './capability-service-definition.component';
import { CapabilityServiceDefinitionDetailComponent } from './capability-service-definition-detail.component';
import { CapabilityServiceDefinitionUpdateComponent } from './capability-service-definition-update.component';
import { CapabilityServiceDefinitionDeleteDialogComponent } from './capability-service-definition-delete-dialog.component';
import { capabilityServiceDefinitionRoute } from './capability-service-definition.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(capabilityServiceDefinitionRoute)],
  declarations: [
    CapabilityServiceDefinitionComponent,
    CapabilityServiceDefinitionDetailComponent,
    CapabilityServiceDefinitionUpdateComponent,
    CapabilityServiceDefinitionDeleteDialogComponent,
  ],
  entryComponents: [CapabilityServiceDefinitionDeleteDialogComponent],
})
export class BoatBayCapabilityServiceDefinitionModule {}
