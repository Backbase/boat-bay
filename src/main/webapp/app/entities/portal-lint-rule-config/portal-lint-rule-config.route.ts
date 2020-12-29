import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPortalLintRuleConfig, PortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';
import { PortalLintRuleConfigService } from './portal-lint-rule-config.service';
import { PortalLintRuleConfigComponent } from './portal-lint-rule-config.component';
import { PortalLintRuleConfigDetailComponent } from './portal-lint-rule-config-detail.component';
import { PortalLintRuleConfigUpdateComponent } from './portal-lint-rule-config-update.component';

@Injectable({ providedIn: 'root' })
export class PortalLintRuleConfigResolve implements Resolve<IPortalLintRuleConfig> {
  constructor(private service: PortalLintRuleConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPortalLintRuleConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((portalLintRuleConfig: HttpResponse<PortalLintRuleConfig>) => {
          if (portalLintRuleConfig.body) {
            return of(portalLintRuleConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PortalLintRuleConfig());
  }
}

export const portalLintRuleConfigRoute: Routes = [
  {
    path: '',
    component: PortalLintRuleConfigComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortalLintRuleConfigDetailComponent,
    resolve: {
      portalLintRuleConfig: PortalLintRuleConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortalLintRuleConfigUpdateComponent,
    resolve: {
      portalLintRuleConfig: PortalLintRuleConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortalLintRuleConfigUpdateComponent,
    resolve: {
      portalLintRuleConfig: PortalLintRuleConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PortalLintRuleConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
];
