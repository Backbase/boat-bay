import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILintRuleSet, LintRuleSet } from 'app/shared/model/lint-rule-set.model';
import { LintRuleSetService } from './lint-rule-set.service';
import { LintRuleSetComponent } from './lint-rule-set.component';
import { LintRuleSetDetailComponent } from './lint-rule-set-detail.component';
import { LintRuleSetUpdateComponent } from './lint-rule-set-update.component';

@Injectable({ providedIn: 'root' })
export class LintRuleSetResolve implements Resolve<ILintRuleSet> {
  constructor(private service: LintRuleSetService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintRuleSet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lintRuleSet: HttpResponse<LintRuleSet>) => {
          if (lintRuleSet.body) {
            return of(lintRuleSet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintRuleSet());
  }
}

export const lintRuleSetRoute: Routes = [
  {
    path: '',
    component: LintRuleSetComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintRuleSetDetailComponent,
    resolve: {
      lintRuleSet: LintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintRuleSetUpdateComponent,
    resolve: {
      lintRuleSet: LintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintRuleSetUpdateComponent,
    resolve: {
      lintRuleSet: LintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
];
