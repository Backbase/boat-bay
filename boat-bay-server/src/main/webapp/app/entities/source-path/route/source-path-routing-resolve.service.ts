import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISourcePath, SourcePath } from '../source-path.model';
import { SourcePathService } from '../service/source-path.service';

@Injectable({ providedIn: 'root' })
export class SourcePathRoutingResolveService implements Resolve<ISourcePath> {
  constructor(protected service: SourcePathService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISourcePath> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sourcePath: HttpResponse<SourcePath>) => {
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
