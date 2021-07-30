import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ZallyConfigComponent } from '../list/zally-config.component';
import { ZallyConfigDetailComponent } from '../detail/zally-config-detail.component';
import { ZallyConfigUpdateComponent } from '../update/zally-config-update.component';
import { ZallyConfigRoutingResolveService } from './zally-config-routing-resolve.service';

const zallyConfigRoute: Routes = [
  {
    path: '',
    component: ZallyConfigComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZallyConfigDetailComponent,
    resolve: {
      zallyConfig: ZallyConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZallyConfigUpdateComponent,
    resolve: {
      zallyConfig: ZallyConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZallyConfigUpdateComponent,
    resolve: {
      zallyConfig: ZallyConfigRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(zallyConfigRoute)],
  exports: [RouterModule],
})
export class ZallyConfigRoutingModule {}
