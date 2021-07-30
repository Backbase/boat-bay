import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IZallyConfig, ZallyConfig } from '../zally-config.model';
import { ZallyConfigService } from '../service/zally-config.service';

@Injectable({ providedIn: 'root' })
export class ZallyConfigRoutingResolveService implements Resolve<IZallyConfig> {
  constructor(protected service: ZallyConfigService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZallyConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((zallyConfig: HttpResponse<ZallyConfig>) => {
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
