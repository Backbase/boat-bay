import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProductRelease, ProductRelease } from 'app/shared/model/product-release.model';
import { ProductReleaseService } from './product-release.service';
import { ProductReleaseComponent } from './product-release.component';
import { ProductReleaseDetailComponent } from './product-release-detail.component';
import { ProductReleaseUpdateComponent } from './product-release-update.component';

@Injectable({ providedIn: 'root' })
export class ProductReleaseResolve implements Resolve<IProductRelease> {
  constructor(private service: ProductReleaseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductRelease> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((productRelease: HttpResponse<ProductRelease>) => {
          if (productRelease.body) {
            return of(productRelease.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductRelease());
  }
}

export const productReleaseRoute: Routes = [
  {
    path: '',
    component: ProductReleaseComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ProductReleases',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductReleaseDetailComponent,
    resolve: {
      productRelease: ProductReleaseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ProductReleases',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductReleaseUpdateComponent,
    resolve: {
      productRelease: ProductReleaseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ProductReleases',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductReleaseUpdateComponent,
    resolve: {
      productRelease: ProductReleaseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ProductReleases',
    },
    canActivate: [UserRouteAccessService],
  },
];
