import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceDefinitionComponent } from '../list/service-definition.component';
import { ServiceDefinitionDetailComponent } from '../detail/service-definition-detail.component';
import { ServiceDefinitionUpdateComponent } from '../update/service-definition-update.component';
import { ServiceDefinitionRoutingResolveService } from './service-definition-routing-resolve.service';

const serviceDefinitionRoute: Routes = [
  {
    path: '',
    component: ServiceDefinitionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceDefinitionDetailComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceDefinitionUpdateComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceDefinitionUpdateComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceDefinitionRoute)],
  exports: [RouterModule],
})
export class ServiceDefinitionRoutingModule {}
