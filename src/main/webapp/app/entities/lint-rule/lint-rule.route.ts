import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILintRule, LintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from './lint-rule.service';
import { LintRuleComponent } from './lint-rule.component';
import { LintRuleDetailComponent } from './lint-rule-detail.component';
import { LintRuleUpdateComponent } from './lint-rule-update.component';

@Injectable({ providedIn: 'root' })
export class LintRuleResolve implements Resolve<ILintRule> {
  constructor(private service: LintRuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lintRule: HttpResponse<LintRule>) => {
          if (lintRule.body) {
            return of(lintRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintRule());
  }
}

export const lintRuleRoute: Routes = [
  {
    path: '',
    component: LintRuleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintRuleDetailComponent,
    resolve: {
      lintRule: LintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintRuleUpdateComponent,
    resolve: {
      lintRule: LintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintRuleUpdateComponent,
    resolve: {
      lintRule: LintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRules',
    },
    canActivate: [UserRouteAccessService],
  },
];
