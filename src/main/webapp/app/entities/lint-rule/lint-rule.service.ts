import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILintRule } from 'app/shared/model/lint-rule.model';

type EntityResponseType = HttpResponse<ILintRule>;
type EntityArrayResponseType = HttpResponse<ILintRule[]>;

@Injectable({ providedIn: 'root' })
export class LintRuleService {
  public resourceUrl = SERVER_API_URL + 'api/lint-rules';

  constructor(protected http: HttpClient) {}

  create(lintRule: ILintRule): Observable<EntityResponseType> {
    return this.http.post<ILintRule>(this.resourceUrl, lintRule, { observe: 'response' });
  }

  update(lintRule: ILintRule): Observable<EntityResponseType> {
    return this.http.put<ILintRule>(this.resourceUrl, lintRule, { observe: 'response' });
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
}
