import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPortal, Portal } from 'app/shared/model/portal.model';
import { PortalService } from './portal.service';
import { PortalComponent } from './portal.component';
import { PortalDetailComponent } from './portal-detail.component';
import { PortalUpdateComponent } from './portal-update.component';

@Injectable({ providedIn: 'root' })
export class PortalResolve implements Resolve<IPortal> {
  constructor(private service: PortalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPortal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((portal: HttpResponse<Portal>) => {
          if (portal.body) {
            return of(portal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Portal());
  }
}

export const portalRoute: Routes = [
  {
    path: '',
    component: PortalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Portals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortalDetailComponent,
    resolve: {
      portal: PortalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Portals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortalUpdateComponent,
    resolve: {
      portal: PortalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Portals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortalUpdateComponent,
    resolve: {
      portal: PortalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Portals',
    },
    canActivate: [UserRouteAccessService],
  },
];
