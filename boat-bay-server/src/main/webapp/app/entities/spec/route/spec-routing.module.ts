import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpecComponent } from '../list/spec.component';
import { SpecDetailComponent } from '../detail/spec-detail.component';
import { SpecUpdateComponent } from '../update/spec-update.component';
import { SpecRoutingResolveService } from './spec-routing-resolve.service';

const specRoute: Routes = [
  {
    path: '',
    component: SpecComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecDetailComponent,
    resolve: {
      spec: SpecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecUpdateComponent,
    resolve: {
      spec: SpecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecUpdateComponent,
    resolve: {
      spec: SpecRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(specRoute)],
  exports: [RouterModule],
})
export class SpecRoutingModule {}
