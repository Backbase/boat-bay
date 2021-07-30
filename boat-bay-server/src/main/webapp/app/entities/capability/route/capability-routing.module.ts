import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CapabilityComponent } from '../list/capability.component';
import { CapabilityDetailComponent } from '../detail/capability-detail.component';
import { CapabilityUpdateComponent } from '../update/capability-update.component';
import { CapabilityRoutingResolveService } from './capability-routing-resolve.service';

const capabilityRoute: Routes = [
  {
    path: '',
    component: CapabilityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapabilityDetailComponent,
    resolve: {
      capability: CapabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapabilityUpdateComponent,
    resolve: {
      capability: CapabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapabilityUpdateComponent,
    resolve: {
      capability: CapabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(capabilityRoute)],
  exports: [RouterModule],
})
export class CapabilityRoutingModule {}
