import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICapability, Capability } from 'app/shared/model/capability.model';
import { CapabilityService } from './capability.service';
import { CapabilityComponent } from './capability.component';
import { CapabilityDetailComponent } from './capability-detail.component';
import { CapabilityUpdateComponent } from './capability-update.component';

@Injectable({ providedIn: 'root' })
export class CapabilityResolve implements Resolve<ICapability> {
  constructor(private service: CapabilityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICapability> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((capability: HttpResponse<Capability>) => {
          if (capability.body) {
            return of(capability.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Capability());
  }
}

export const capabilityRoute: Routes = [
  {
    path: '',
    component: CapabilityComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Capabilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapabilityDetailComponent,
    resolve: {
      capability: CapabilityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Capabilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapabilityUpdateComponent,
    resolve: {
      capability: CapabilityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Capabilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapabilityUpdateComponent,
    resolve: {
      capability: CapabilityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Capabilities',
    },
    canActivate: [UserRouteAccessService],
  },
];
