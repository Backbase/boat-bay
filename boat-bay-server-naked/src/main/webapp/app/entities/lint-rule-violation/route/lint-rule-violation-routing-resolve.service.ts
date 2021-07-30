import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILintRuleViolation, LintRuleViolation } from '../lint-rule-violation.model';
import { LintRuleViolationService } from '../service/lint-rule-violation.service';

@Injectable({ providedIn: 'root' })
export class LintRuleViolationRoutingResolveService implements Resolve<ILintRuleViolation> {
  constructor(protected service: LintRuleViolationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintRuleViolation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lintRuleViolation: HttpResponse<LintRuleViolation>) => {
          if (lintRuleViolation.body) {
            return of(lintRuleViolation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintRuleViolation());
  }
}
