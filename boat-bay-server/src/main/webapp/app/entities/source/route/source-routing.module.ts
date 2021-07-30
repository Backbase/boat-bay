import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SourceComponent } from '../list/source.component';
import { SourceDetailComponent } from '../detail/source-detail.component';
import { SourceUpdateComponent } from '../update/source-update.component';
import { SourceRoutingResolveService } from './source-routing-resolve.service';

const sourceRoute: Routes = [
  {
    path: '',
    component: SourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourceDetailComponent,
    resolve: {
      source: SourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourceUpdateComponent,
    resolve: {
      source: SourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourceUpdateComponent,
    resolve: {
      source: SourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sourceRoute)],
  exports: [RouterModule],
})
export class SourceRoutingModule {}
