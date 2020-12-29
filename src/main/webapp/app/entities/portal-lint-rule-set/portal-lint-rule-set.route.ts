import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPortalLintRuleSet, PortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';
import { PortalLintRuleSetService } from './portal-lint-rule-set.service';
import { PortalLintRuleSetComponent } from './portal-lint-rule-set.component';
import { PortalLintRuleSetDetailComponent } from './portal-lint-rule-set-detail.component';
import { PortalLintRuleSetUpdateComponent } from './portal-lint-rule-set-update.component';

@Injectable({ providedIn: 'root' })
export class PortalLintRuleSetResolve implements Resolve<IPortalLintRuleSet> {
  constructor(private service: PortalLintRuleSetService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPortalLintRuleSet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((portalLintRuleSet: HttpResponse<PortalLintRuleSet>) => {
          if (portalLintRuleSet.body) {
            return of(portalLintRuleSet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PortalLintRuleSet());
  }
}

export const portalLintRuleSetRoute: Routes = [
  {
    path: '',
    component: PortalLintRuleSetComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortalLintRuleSetDetailComponent,
    resolve: {
      portalLintRuleSet: PortalLintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortalLintRuleSetUpdateComponent,
    resolve: {
      portalLintRuleSet: PortalLintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortalLintRuleSetUpdateComponent,
    resolve: {
      portalLintRuleSet: PortalLintRuleSetResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleSets',
    },
    canActivate: [UserRouteAccessService],
  },
];
