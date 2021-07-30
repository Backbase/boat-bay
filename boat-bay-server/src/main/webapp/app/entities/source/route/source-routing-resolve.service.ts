import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISource, Source } from '../source.model';
import { SourceService } from '../service/source.service';

@Injectable({ providedIn: 'root' })
export class SourceRoutingResolveService implements Resolve<ISource> {
  constructor(protected service: SourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((source: HttpResponse<Source>) => {
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
