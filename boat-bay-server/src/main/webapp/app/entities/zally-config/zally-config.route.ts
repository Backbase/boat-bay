import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IZallyConfig, ZallyConfig } from 'app/shared/model/zally-config.model';
import { ZallyConfigService } from './zally-config.service';
import { ZallyConfigComponent } from './zally-config.component';
import { ZallyConfigDetailComponent } from './zally-config-detail.component';
import { ZallyConfigUpdateComponent } from './zally-config-update.component';

@Injectable({ providedIn: 'root' })
export class ZallyConfigResolve implements Resolve<IZallyConfig> {
  constructor(private service: ZallyConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZallyConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((zallyConfig: HttpResponse<ZallyConfig>) => {
          if (zallyConfig.body) {
            return of(zallyConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ZallyConfig());
  }
}

export const zallyConfigRoute: Routes = [
  {
    path: '',
    component: ZallyConfigComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ZallyConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZallyConfigDetailComponent,
    resolve: {
      zallyConfig: ZallyConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ZallyConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZallyConfigUpdateComponent,
    resolve: {
      zallyConfig: ZallyConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ZallyConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZallyConfigUpdateComponent,
    resolve: {
      zallyConfig: ZallyConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ZallyConfigs',
    },
    canActivate: [UserRouteAccessService],
  },
];
