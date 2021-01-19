import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { TagComponent } from './tag.component';
import { TagDetailComponent } from './tag-detail.component';
import { TagUpdateComponent } from './tag-update.component';

@Injectable({ providedIn: 'root' })
export class TagResolve implements Resolve<ITag> {
  constructor(private service: TagService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((tag: HttpResponse<Tag>) => {
          if (tag.body) {
            return of(tag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tag());
  }
}

export const tagRoute: Routes = [
  {
    path: '',
    component: TagComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Tags',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TagDetailComponent,
    resolve: {
      tag: TagResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Tags',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TagUpdateComponent,
    resolve: {
      tag: TagResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Tags',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TagUpdateComponent,
    resolve: {
      tag: TagResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Tags',
    },
    canActivate: [UserRouteAccessService],
  },
];
