import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';

type EntityResponseType = HttpResponse<ILintRuleSet>;
type EntityArrayResponseType = HttpResponse<ILintRuleSet[]>;

@Injectable({ providedIn: 'root' })
export class LintRuleSetService {
  public resourceUrl = SERVER_API_URL + 'api/lint-rule-sets';

  constructor(protected http: HttpClient) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILintRuleSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILintRuleSet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
