import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SourcePathComponent } from '../list/source-path.component';
import { SourcePathDetailComponent } from '../detail/source-path-detail.component';
import { SourcePathUpdateComponent } from '../update/source-path-update.component';
import { SourcePathRoutingResolveService } from './source-path-routing-resolve.service';

const sourcePathRoute: Routes = [
  {
    path: '',
    component: SourcePathComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourcePathDetailComponent,
    resolve: {
      sourcePath: SourcePathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourcePathUpdateComponent,
    resolve: {
      sourcePath: SourcePathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourcePathUpdateComponent,
    resolve: {
      sourcePath: SourcePathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sourcePathRoute)],
  exports: [RouterModule],
})
export class SourcePathRoutingModule {}
