import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILintRule, LintRule } from '../lint-rule.model';
import { LintRuleService } from '../service/lint-rule.service';

@Injectable({ providedIn: 'root' })
export class LintRuleRoutingResolveService implements Resolve<ILintRule> {
  constructor(protected service: LintRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILintRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lintRule: HttpResponse<LintRule>) => {
          if (lintRule.body) {
            return of(lintRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LintRule());
  }
}
