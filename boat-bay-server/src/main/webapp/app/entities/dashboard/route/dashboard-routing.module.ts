import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DashboardComponent } from '../list/dashboard.component';
import { DashboardDetailComponent } from '../detail/dashboard-detail.component';
import { DashboardUpdateComponent } from '../update/dashboard-update.component';
import { DashboardRoutingResolveService } from './dashboard-routing-resolve.service';

const dashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DashboardDetailComponent,
    resolve: {
      dashboard: DashboardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DashboardUpdateComponent,
    resolve: {
      dashboard: DashboardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DashboardUpdateComponent,
    resolve: {
      dashboard: DashboardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dashboardRoute)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
