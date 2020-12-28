import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICapabilityServiceDefinition, CapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { CapabilityServiceDefinitionService } from './capability-service-definition.service';
import { CapabilityServiceDefinitionComponent } from './capability-service-definition.component';
import { CapabilityServiceDefinitionDetailComponent } from './capability-service-definition-detail.component';
import { CapabilityServiceDefinitionUpdateComponent } from './capability-service-definition-update.component';

@Injectable({ providedIn: 'root' })
export class CapabilityServiceDefinitionResolve implements Resolve<ICapabilityServiceDefinition> {
  constructor(private service: CapabilityServiceDefinitionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICapabilityServiceDefinition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((capabilityServiceDefinition: HttpResponse<CapabilityServiceDefinition>) => {
          if (capabilityServiceDefinition.body) {
            return of(capabilityServiceDefinition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CapabilityServiceDefinition());
  }
}

export const capabilityServiceDefinitionRoute: Routes = [
  {
    path: '',
    component: CapabilityServiceDefinitionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CapabilityServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapabilityServiceDefinitionDetailComponent,
    resolve: {
      capabilityServiceDefinition: CapabilityServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CapabilityServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapabilityServiceDefinitionUpdateComponent,
    resolve: {
      capabilityServiceDefinition: CapabilityServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CapabilityServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapabilityServiceDefinitionUpdateComponent,
    resolve: {
      capabilityServiceDefinition: CapabilityServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CapabilityServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
];
