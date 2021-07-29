import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPortal, Portal } from '../portal.model';
import { PortalService } from '../service/portal.service';

@Injectable({ providedIn: 'root' })
export class PortalRoutingResolveService implements Resolve<IPortal> {
  constructor(protected service: PortalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPortal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((portal: HttpResponse<Portal>) => {
          if (portal.body) {
            return of(portal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Portal());
  }
}
