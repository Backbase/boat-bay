import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ServiceDefinitionComponent } from './list/service-definition.component';
import { ServiceDefinitionDetailComponent } from './detail/service-definition-detail.component';
import { ServiceDefinitionUpdateComponent } from './update/service-definition-update.component';
import { ServiceDefinitionDeleteDialogComponent } from './delete/service-definition-delete-dialog.component';
import { ServiceDefinitionRoutingModule } from './route/service-definition-routing.module';

@NgModule({
  imports: [SharedModule, ServiceDefinitionRoutingModule],
  declarations: [
    ServiceDefinitionComponent,
    ServiceDefinitionDetailComponent,
    ServiceDefinitionUpdateComponent,
    ServiceDefinitionDeleteDialogComponent,
  ],
  entryComponents: [ServiceDefinitionDeleteDialogComponent],
})
export class ServiceDefinitionModule {}
