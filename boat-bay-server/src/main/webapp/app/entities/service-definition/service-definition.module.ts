import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { ServiceDefinitionComponent } from './service-definition.component';
import { ServiceDefinitionDetailComponent } from './service-definition-detail.component';
import { ServiceDefinitionUpdateComponent } from './service-definition-update.component';
import { ServiceDefinitionDeleteDialogComponent } from './service-definition-delete-dialog.component';
import { serviceDefinitionRoute } from './service-definition.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(serviceDefinitionRoute)],
  declarations: [
    ServiceDefinitionComponent,
    ServiceDefinitionDetailComponent,
    ServiceDefinitionUpdateComponent,
    ServiceDefinitionDeleteDialogComponent,
  ],
  entryComponents: [ServiceDefinitionDeleteDialogComponent],
})
export class BoatBayServiceDefinitionModule {}
