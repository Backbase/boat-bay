import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LintReportComponent } from '../list/lint-report.component';
import { LintReportDetailComponent } from '../detail/lint-report-detail.component';
import { LintReportUpdateComponent } from '../update/lint-report-update.component';
import { LintReportRoutingResolveService } from './lint-report-routing-resolve.service';

const lintReportRoute: Routes = [
  {
    path: '',
    component: LintReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintReportDetailComponent,
    resolve: {
      lintReport: LintReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintReportUpdateComponent,
    resolve: {
      lintReport: LintReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintReportUpdateComponent,
    resolve: {
      lintReport: LintReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lintReportRoute)],
  exports: [RouterModule],
})
export class LintReportRoutingModule {}
