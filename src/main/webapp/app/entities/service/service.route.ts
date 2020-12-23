import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IService, Service } from 'app/shared/model/service.model';
import { ServiceService } from './service.service';
import { ServiceComponent } from './service.component';
import { ServiceDetailComponent } from './service-detail.component';
import { ServiceUpdateComponent } from './service-update.component';

@Injectable({ providedIn: 'root' })
export class ServiceResolve implements Resolve<IService> {
  constructor(private service: ServiceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((service: HttpResponse<Service>) => {
          if (service.body) {
            return of(service.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Service());
  }
}

export const serviceRoute: Routes = [
  {
    path: '',
    component: ServiceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Services',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceDetailComponent,
    resolve: {
      service: ServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Services',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceUpdateComponent,
    resolve: {
      service: ServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Services',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceUpdateComponent,
    resolve: {
      service: ServiceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Services',
    },
    canActivate: [UserRouteAccessService],
  },
];
