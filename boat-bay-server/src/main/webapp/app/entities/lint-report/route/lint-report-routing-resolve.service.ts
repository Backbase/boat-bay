import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILintReport, LintReport } from '../lint-report.model';
import { LintReportService } from '../service/lint-report.service';

@Injectable({ providedIn: 'root' })
export class LintReportRoutingResolveService implements Resolve<ILintReport> {
  constructor(protected service: LintReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lintReport: HttpResponse<LintReport>) => {
          if (lintReport.body) {
            return of(lintReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintReport());
  }
}
