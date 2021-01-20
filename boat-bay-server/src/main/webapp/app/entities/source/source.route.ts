import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISource, Source } from 'app/shared/model/source.model';
import { SourceService } from './source.service';
import { SourceComponent } from './source.component';
import { SourceDetailComponent } from './source-detail.component';
import { SourceUpdateComponent } from './source-update.component';

@Injectable({ providedIn: 'root' })
export class SourceResolve implements Resolve<ISource> {
  constructor(private service: SourceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((source: HttpResponse<Source>) => {
          if (source.body) {
            return of(source.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Source());
  }
}

export const sourceRoute: Routes = [
  {
    path: '',
    component: SourceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sources',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourceDetailComponent,
    resolve: {
      source: SourceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sources',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourceUpdateComponent,
    resolve: {
      source: SourceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sources',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourceUpdateComponent,
    resolve: {
      source: SourceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Sources',
    },
    canActivate: [UserRouteAccessService],
  },
];
