import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PortalComponent } from '../list/portal.component';
import { PortalDetailComponent } from '../detail/portal-detail.component';
import { PortalUpdateComponent } from '../update/portal-update.component';
import { PortalRoutingResolveService } from './portal-routing-resolve.service';

const portalRoute: Routes = [
  {
    path: '',
    component: PortalComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortalDetailComponent,
    resolve: {
      portal: PortalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortalUpdateComponent,
    resolve: {
      portal: PortalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortalUpdateComponent,
    resolve: {
      portal: PortalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(portalRoute)],
  exports: [RouterModule],
})
export class PortalRoutingModule {}
