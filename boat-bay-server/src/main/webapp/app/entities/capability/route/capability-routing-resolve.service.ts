import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICapability, Capability } from '../capability.model';
import { CapabilityService } from '../service/capability.service';

@Injectable({ providedIn: 'root' })
export class CapabilityRoutingResolveService implements Resolve<ICapability> {
  constructor(protected service: CapabilityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICapability> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((capability: HttpResponse<Capability>) => {
          if (capability.body) {
            return of(capability.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Capability());
  }
}
