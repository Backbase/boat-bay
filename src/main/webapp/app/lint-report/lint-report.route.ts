import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot, Routes } from '@angular/router';
import { LintReport } from '../models/lint-report';
import { EMPTY, Observable, of } from 'rxjs';
import { LintReportService } from '../services/lint-report.service';
import { flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { LintReportListComponent } from '../lint-report-list/lint-report-list.component';
import { Authority } from '../shared/constants/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';

@Injectable({ providedIn: 'root' })
export class LintReportResolve implements Resolve<LintReport> {
  constructor(private service: LintReportService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<LintReport> | Promise<LintReport> | LintReport {
    const id = route.params['id'];
    if (id) {
      return this.service.get(id).pipe(
        flatMap((response: HttpResponse<LintReport>) => {
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

export const capabilityRoute: Routes = [
  {
    path: '',
    component: LintReportListComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Lint Reports',
    },
    canActivate: [UserRouteAccessService],
  },
  {},
];
