import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LintRuleViolationComponent } from '../list/lint-rule-violation.component';
import { LintRuleViolationDetailComponent } from '../detail/lint-rule-violation-detail.component';
import { LintRuleViolationUpdateComponent } from '../update/lint-rule-violation-update.component';
import { LintRuleViolationRoutingResolveService } from './lint-rule-violation-routing-resolve.service';

const lintRuleViolationRoute: Routes = [
  {
    path: '',
    component: LintRuleViolationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintRuleViolationDetailComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintRuleViolationUpdateComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintRuleViolationUpdateComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lintRuleViolationRoute)],
  exports: [RouterModule],
})
export class LintRuleViolationRoutingModule {}
