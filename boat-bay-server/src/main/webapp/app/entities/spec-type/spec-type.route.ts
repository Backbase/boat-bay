import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISpecType, SpecType } from 'app/shared/model/spec-type.model';
import { SpecTypeService } from './spec-type.service';
import { SpecTypeComponent } from './spec-type.component';
import { SpecTypeDetailComponent } from './spec-type-detail.component';
import { SpecTypeUpdateComponent } from './spec-type-update.component';

@Injectable({ providedIn: 'root' })
export class SpecTypeResolve implements Resolve<ISpecType> {
  constructor(private service: SpecTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((specType: HttpResponse<SpecType>) => {
          if (specType.body) {
            return of(specType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SpecType());
  }
}

export const specTypeRoute: Routes = [
  {
    path: '',
    component: SpecTypeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SpecTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecTypeDetailComponent,
    resolve: {
      specType: SpecTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SpecTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecTypeUpdateComponent,
    resolve: {
      specType: SpecTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SpecTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecTypeUpdateComponent,
    resolve: {
      specType: SpecTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SpecTypes',
    },
    canActivate: [UserRouteAccessService],
  },
];
