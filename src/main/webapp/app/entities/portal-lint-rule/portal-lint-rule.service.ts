import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';

type EntityResponseType = HttpResponse<IPortalLintRule>;
type EntityArrayResponseType = HttpResponse<IPortalLintRule[]>;

@Injectable({ providedIn: 'root' })
export class PortalLintRuleService {
  public resourceUrl = SERVER_API_URL + 'api/portal-lint-rules';

  constructor(protected http: HttpClient) {}

  create(portalLintRule: IPortalLintRule): Observable<EntityResponseType> {
    return this.http.post<IPortalLintRule>(this.resourceUrl, portalLintRule, { observe: 'response' });
  }

  update(portalLintRule: IPortalLintRule): Observable<EntityResponseType> {
    return this.http.put<IPortalLintRule>(this.resourceUrl, portalLintRule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPortalLintRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPortalLintRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
