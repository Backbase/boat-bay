import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IServiceDefinition, ServiceDefinition } from 'app/shared/model/service-definition.model';
import { ServiceDefinitionService } from './service-definition.service';
import { ServiceDefinitionComponent } from './service-definition.component';
import { ServiceDefinitionDetailComponent } from './service-definition-detail.component';
import { ServiceDefinitionUpdateComponent } from './service-definition-update.component';

@Injectable({ providedIn: 'root' })
export class ServiceDefinitionResolve implements Resolve<IServiceDefinition> {
  constructor(private service: ServiceDefinitionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceDefinition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((serviceDefinition: HttpResponse<ServiceDefinition>) => {
          if (serviceDefinition.body) {
            return of(serviceDefinition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceDefinition());
  }
}

export const serviceDefinitionRoute: Routes = [
  {
    path: '',
    component: ServiceDefinitionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceDefinitionDetailComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceDefinitionUpdateComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceDefinitionUpdateComponent,
    resolve: {
      serviceDefinition: ServiceDefinitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceDefinitions',
    },
    canActivate: [UserRouteAccessService],
  },
];
