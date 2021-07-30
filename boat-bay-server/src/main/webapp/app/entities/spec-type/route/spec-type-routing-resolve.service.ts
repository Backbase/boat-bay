import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecType, SpecType } from '../spec-type.model';
import { SpecTypeService } from '../service/spec-type.service';

@Injectable({ providedIn: 'root' })
export class SpecTypeRoutingResolveService implements Resolve<ISpecType> {
  constructor(protected service: SpecTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((specType: HttpResponse<SpecType>) => {
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
