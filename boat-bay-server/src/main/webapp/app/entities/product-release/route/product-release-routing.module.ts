import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductReleaseComponent } from '../list/product-release.component';
import { ProductReleaseDetailComponent } from '../detail/product-release-detail.component';
import { ProductReleaseUpdateComponent } from '../update/product-release-update.component';
import { ProductReleaseRoutingResolveService } from './product-release-routing-resolve.service';

const productReleaseRoute: Routes = [
  {
    path: '',
    component: ProductReleaseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductReleaseDetailComponent,
    resolve: {
      productRelease: ProductReleaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductReleaseUpdateComponent,
    resolve: {
      productRelease: ProductReleaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductReleaseUpdateComponent,
    resolve: {
      productRelease: ProductReleaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productReleaseRoute)],
  exports: [RouterModule],
})
export class ProductReleaseRoutingModule {}
