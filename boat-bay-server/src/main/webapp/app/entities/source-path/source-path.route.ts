import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISourcePath, SourcePath } from 'app/shared/model/source-path.model';
import { SourcePathService } from './source-path.service';
import { SourcePathComponent } from './source-path.component';
import { SourcePathDetailComponent } from './source-path-detail.component';
import { SourcePathUpdateComponent } from './source-path-update.component';

@Injectable({ providedIn: 'root' })
export class SourcePathResolve implements Resolve<ISourcePath> {
  constructor(private service: SourcePathService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISourcePath> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sourcePath: HttpResponse<SourcePath>) => {
          if (sourcePath.body) {
            return of(sourcePath.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SourcePath());
  }
}

export const sourcePathRoute: Routes = [
  {
    path: '',
    component: SourcePathComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SourcePaths',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourcePathDetailComponent,
    resolve: {
      sourcePath: SourcePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SourcePaths',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourcePathUpdateComponent,
    resolve: {
      sourcePath: SourcePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SourcePaths',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourcePathUpdateComponent,
    resolve: {
      sourcePath: SourcePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SourcePaths',
    },
    canActivate: [UserRouteAccessService],
  },
];
