import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpecTypeComponent } from '../list/spec-type.component';
import { SpecTypeDetailComponent } from '../detail/spec-type-detail.component';
import { SpecTypeUpdateComponent } from '../update/spec-type-update.component';
import { SpecTypeRoutingResolveService } from './spec-type-routing-resolve.service';

const specTypeRoute: Routes = [
  {
    path: '',
    component: SpecTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecTypeDetailComponent,
    resolve: {
      specType: SpecTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecTypeUpdateComponent,
    resolve: {
      specType: SpecTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecTypeUpdateComponent,
    resolve: {
      specType: SpecTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(specTypeRoute)],
  exports: [RouterModule],
})
export class SpecTypeRoutingModule {}
