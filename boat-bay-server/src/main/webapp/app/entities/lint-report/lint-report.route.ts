import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILintReport, LintReport } from 'app/shared/model/lint-report.model';
import { LintReportService } from './lint-report.service';
import { LintReportComponent } from './lint-report.component';
import { LintReportDetailComponent } from './lint-report-detail.component';
import { LintReportUpdateComponent } from './lint-report-update.component';

@Injectable({ providedIn: 'root' })
export class LintReportResolve implements Resolve<ILintReport> {
  constructor(private service: LintReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lintReport: HttpResponse<LintReport>) => {
          if (lintReport.body) {
            return of(lintReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintReport());
  }
}

export const lintReportRoute: Routes = [
  {
    path: '',
    component: LintReportComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintReports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintReportDetailComponent,
    resolve: {
      lintReport: LintReportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintReports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintReportUpdateComponent,
    resolve: {
      lintReport: LintReportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintReports',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintReportUpdateComponent,
    resolve: {
      lintReport: LintReportResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintReports',
    },
    canActivate: [UserRouteAccessService],
  },
];
