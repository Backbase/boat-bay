import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Authority } from '../shared/constants/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { BoatQuayReportComponent } from '../boat-quay-report/boat-quay-report.component';
import { BoatQuayDashboardComponent } from 'app/boat-quay-dashboard/boat-quay-dashboard.component';
import { BoatLintReport } from 'app/models/lint-report';
import { BoatLintReportService } from 'app/services/boat-lint-report.service';

@Injectable({ providedIn: 'root' })
export class BoatQuayReportResolve implements Resolve<BoatLintReport> {
  constructor(private service: BoatLintReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<BoatLintReport> | Promise<BoatLintReport> | BoatLintReport {
    const id = route.params['id'];
    if (id) {
      return this.service.getReport(id).pipe(
        flatMap((response: HttpResponse<BoatLintReport>) => {
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

export const boatQuayRoutes: Routes = [
  {
    path: '',
    component: BoatQuayDashboardComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Lint Reports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'report/:id',
    component: BoatQuayReportComponent,
    resolve: {
      lintReport: BoatQuayReportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Lint Report',
    },
    canActivate: [UserRouteAccessService],
  },
];
