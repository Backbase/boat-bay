import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDashboard, Dashboard } from 'app/shared/model/dashboard.model';
import { DashboardService } from './dashboard.service';
import { DashboardComponent } from './dashboard.component';
import { DashboardDetailComponent } from './dashboard-detail.component';
import { DashboardUpdateComponent } from './dashboard-update.component';

@Injectable({ providedIn: 'root' })
export class DashboardResolve implements Resolve<IDashboard> {
  constructor(private service: DashboardService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDashboard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dashboard: HttpResponse<Dashboard>) => {
          if (dashboard.body) {
            return of(dashboard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dashboard());
  }
}

export const dashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboards',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DashboardDetailComponent,
    resolve: {
      dashboard: DashboardResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboards',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DashboardUpdateComponent,
    resolve: {
      dashboard: DashboardResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboards',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DashboardUpdateComponent,
    resolve: {
      dashboard: DashboardResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dashboards',
    },
    canActivate: [UserRouteAccessService],
  },
];
