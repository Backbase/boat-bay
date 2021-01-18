import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPortalLintRule, PortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from './portal-lint-rule.service';
import { PortalLintRuleComponent } from './portal-lint-rule.component';
import { PortalLintRuleDetailComponent } from './portal-lint-rule-detail.component';
import { PortalLintRuleUpdateComponent } from './portal-lint-rule-update.component';

@Injectable({ providedIn: 'root' })
export class PortalLintRuleResolve implements Resolve<IPortalLintRule> {
  constructor(private service: PortalLintRuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPortalLintRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((portalLintRule: HttpResponse<PortalLintRule>) => {
          if (portalLintRule.body) {
            return of(portalLintRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PortalLintRule());
  }
}

export const portalLintRuleRoute: Routes = [
  {
    path: '',
    component: PortalLintRuleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortalLintRuleDetailComponent,
    resolve: {
      portalLintRule: PortalLintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortalLintRuleUpdateComponent,
    resolve: {
      portalLintRule: PortalLintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortalLintRuleUpdateComponent,
    resolve: {
      portalLintRule: PortalLintRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRules',
    },
    canActivate: [UserRouteAccessService],
  },
];
