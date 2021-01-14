import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';

import { HomeComponent } from './home.component';
import { BoatProductDashboard } from 'app/models/dashboard/boat-product-dashboard';
import { BoatDashboardService } from 'app/services/boat-dashboard.service';
import { EMPTY, Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Authority } from 'app/shared/constants/authority.constants';
import { BbProductDashboardComponent } from 'app/bb-product-dashboard/bb-product-dashboard.component';

@Injectable({ providedIn: 'root' })
export class BoatProductDashboardResolver implements Resolve<BoatProductDashboard> {
  constructor(protected portalService: BoatDashboardService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<BoatProductDashboard> | Promise<BoatProductDashboard> | BoatProductDashboard {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    if (portalKey && productKey) {
      return this.portalService.getBoatProductDashboardView(portalKey, productKey).pipe(
        flatMap((response: HttpResponse<BoatProductDashboard>) => {
          if (response.body) {
            return of(response.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    this.router.navigate(['404']);
    return EMPTY;
  }
}

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      authorities: [],
      pageTitle: 'Boat Bay',
    },
  },
  {
    path: 'dashboard/:portalKey/:productKey',
    component: BbProductDashboardComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Boat Bay',
    },
    resolve: {
      productDashboard: BoatProductDashboardResolver,
    },
  },
];
