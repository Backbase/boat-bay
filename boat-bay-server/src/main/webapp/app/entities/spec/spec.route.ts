import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISpec, Spec } from 'app/shared/model/spec.model';
import { SpecService } from './spec.service';
import { SpecComponent } from './spec.component';
import { SpecDetailComponent } from './spec-detail.component';
import { SpecUpdateComponent } from './spec-update.component';

@Injectable({ providedIn: 'root' })
export class SpecResolve implements Resolve<ISpec> {
  constructor(private service: SpecService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpec> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((spec: HttpResponse<Spec>) => {
          if (spec.body) {
            return of(spec.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Spec());
  }
}

export const specRoute: Routes = [
  {
    path: '',
    component: SpecComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Specs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecDetailComponent,
    resolve: {
      spec: SpecResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Specs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecUpdateComponent,
    resolve: {
      spec: SpecResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Specs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecUpdateComponent,
    resolve: {
      spec: SpecResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Specs',
    },
    canActivate: [UserRouteAccessService],
  },
];
