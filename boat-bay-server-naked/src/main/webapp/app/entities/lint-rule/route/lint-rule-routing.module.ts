import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LintRuleComponent } from '../list/lint-rule.component';
import { LintRuleDetailComponent } from '../detail/lint-rule-detail.component';
import { LintRuleUpdateComponent } from '../update/lint-rule-update.component';
import { LintRuleRoutingResolveService } from './lint-rule-routing-resolve.service';

const lintRuleRoute: Routes = [
  {
    path: '',
    component: LintRuleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintRuleDetailComponent,
    resolve: {
      lintRule: LintRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintRuleUpdateComponent,
    resolve: {
      lintRule: LintRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintRuleUpdateComponent,
    resolve: {
      lintRule: LintRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lintRuleRoute)],
  exports: [RouterModule],
})
export class LintRuleRoutingModule {}
