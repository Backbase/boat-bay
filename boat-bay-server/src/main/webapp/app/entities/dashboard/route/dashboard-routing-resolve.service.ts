import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDashboard, Dashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

@Injectable({ providedIn: 'root' })
export class DashboardRoutingResolveService implements Resolve<IDashboard> {
  constructor(protected service: DashboardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDashboard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dashboard: HttpResponse<Dashboard>) => {
          if (dashboard.body) {
            return of(dashboard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dashboard());
  }
}
