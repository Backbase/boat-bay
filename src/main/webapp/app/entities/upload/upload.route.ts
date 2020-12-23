import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUpload, Upload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';
import { UploadComponent } from './upload.component';
import { UploadDetailComponent } from './upload-detail.component';
import { UploadUpdateComponent } from './upload-update.component';

@Injectable({ providedIn: 'root' })
export class UploadResolve implements Resolve<IUpload> {
  constructor(private service: UploadService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUpload> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((upload: HttpResponse<Upload>) => {
          if (upload.body) {
            return of(upload.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Upload());
  }
}

export const uploadRoute: Routes = [
  {
    path: '',
    component: UploadComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Uploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UploadDetailComponent,
    resolve: {
      upload: UploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Uploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UploadUpdateComponent,
    resolve: {
      upload: UploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Uploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UploadUpdateComponent,
    resolve: {
      upload: UploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Uploads',
    },
    canActivate: [UserRouteAccessService],
  },
];
