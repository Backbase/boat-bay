import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILintRule, getLintRuleIdentifier } from '../lint-rule.model';

export type EntityResponseType = HttpResponse<ILintRule>;
export type EntityArrayResponseType = HttpResponse<ILintRule[]>;

@Injectable({ providedIn: 'root' })
export class LintRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lint-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lintRule: ILintRule): Observable<EntityResponseType> {
    return this.http.post<ILintRule>(this.resourceUrl, lintRule, { observe: 'response' });
  }

  update(lintRule: ILintRule): Observable<EntityResponseType> {
    return this.http.put<ILintRule>(`${this.resourceUrl}/${getLintRuleIdentifier(lintRule) as number}`, lintRule, { observe: 'response' });
  }

  partialUpdate(lintRule: ILintRule): Observable<EntityResponseType> {
    return this.http.patch<ILintRule>(`${this.resourceUrl}/${getLintRuleIdentifier(lintRule) as number}`, lintRule, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILintRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILintRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLintRuleToCollectionIfMissing(lintRuleCollection: ILintRule[], ...lintRulesToCheck: (ILintRule | null | undefined)[]): ILintRule[] {
    const lintRules: ILintRule[] = lintRulesToCheck.filter(isPresent);
    if (lintRules.length > 0) {
      const lintRuleCollectionIdentifiers = lintRuleCollection.map(lintRuleItem => getLintRuleIdentifier(lintRuleItem)!);
      const lintRulesToAdd = lintRules.filter(lintRuleItem => {
        const lintRuleIdentifier = getLintRuleIdentifier(lintRuleItem);
        if (lintRuleIdentifier == null || lintRuleCollectionIdentifiers.includes(lintRuleIdentifier)) {
          return false;
        }
        lintRuleCollectionIdentifiers.push(lintRuleIdentifier);
        return true;
      });
      return [...lintRulesToAdd, ...lintRuleCollection];
    }
    return lintRuleCollection;
  }
}
