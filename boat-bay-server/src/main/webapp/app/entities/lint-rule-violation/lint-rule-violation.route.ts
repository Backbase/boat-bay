import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILintRuleViolation, LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from './lint-rule-violation.service';
import { LintRuleViolationComponent } from './lint-rule-violation.component';
import { LintRuleViolationDetailComponent } from './lint-rule-violation-detail.component';
import { LintRuleViolationUpdateComponent } from './lint-rule-violation-update.component';

@Injectable({ providedIn: 'root' })
export class LintRuleViolationResolve implements Resolve<ILintRuleViolation> {
  constructor(private service: LintRuleViolationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintRuleViolation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lintRuleViolation: HttpResponse<LintRuleViolation>) => {
          if (lintRuleViolation.body) {
            return of(lintRuleViolation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintRuleViolation());
  }
}

export const lintRuleViolationRoute: Routes = [
  {
    path: '',
    component: LintRuleViolationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleViolations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LintRuleViolationDetailComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleViolations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LintRuleViolationUpdateComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleViolations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LintRuleViolationUpdateComponent,
    resolve: {
      lintRuleViolation: LintRuleViolationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LintRuleViolations',
    },
    canActivate: [UserRouteAccessService],
  },
];
