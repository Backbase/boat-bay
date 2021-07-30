import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILintRuleViolation, getLintRuleViolationIdentifier } from '../lint-rule-violation.model';

export type EntityResponseType = HttpResponse<ILintRuleViolation>;
export type EntityArrayResponseType = HttpResponse<ILintRuleViolation[]>;

@Injectable({ providedIn: 'root' })
export class LintRuleViolationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lint-rule-violations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lintRuleViolation: ILintRuleViolation): Observable<EntityResponseType> {
    return this.http.post<ILintRuleViolation>(this.resourceUrl, lintRuleViolation, { observe: 'response' });
  }

  update(lintRuleViolation: ILintRuleViolation): Observable<EntityResponseType> {
    return this.http.put<ILintRuleViolation>(
      `${this.resourceUrl}/${getLintRuleViolationIdentifier(lintRuleViolation) as number}`,
      lintRuleViolation,
      { observe: 'response' }
    );
  }

  partialUpdate(lintRuleViolation: ILintRuleViolation): Observable<EntityResponseType> {
    return this.http.patch<ILintRuleViolation>(
      `${this.resourceUrl}/${getLintRuleViolationIdentifier(lintRuleViolation) as number}`,
      lintRuleViolation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILintRuleViolation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILintRuleViolation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLintRuleViolationToCollectionIfMissing(
    lintRuleViolationCollection: ILintRuleViolation[],
    ...lintRuleViolationsToCheck: (ILintRuleViolation | null | undefined)[]
  ): ILintRuleViolation[] {
    const lintRuleViolations: ILintRuleViolation[] = lintRuleViolationsToCheck.filter(isPresent);
    if (lintRuleViolations.length > 0) {
      const lintRuleViolationCollectionIdentifiers = lintRuleViolationCollection.map(
        lintRuleViolationItem => getLintRuleViolationIdentifier(lintRuleViolationItem)!
      );
      const lintRuleViolationsToAdd = lintRuleViolations.filter(lintRuleViolationItem => {
        const lintRuleViolationIdentifier = getLintRuleViolationIdentifier(lintRuleViolationItem);
        if (lintRuleViolationIdentifier == null || lintRuleViolationCollectionIdentifiers.includes(lintRuleViolationIdentifier)) {
          return false;
        }
        lintRuleViolationCollectionIdentifiers.push(lintRuleViolationIdentifier);
        return true;
      });
      return [...lintRuleViolationsToAdd, ...lintRuleViolationCollection];
    }
    return lintRuleViolationCollection;
  }
}
