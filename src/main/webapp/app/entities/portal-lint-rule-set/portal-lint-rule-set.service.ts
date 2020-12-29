import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

type EntityResponseType = HttpResponse<IPortalLintRuleSet>;
type EntityArrayResponseType = HttpResponse<IPortalLintRuleSet[]>;

@Injectable({ providedIn: 'root' })
export class PortalLintRuleSetService {
  public resourceUrl = SERVER_API_URL + 'api/portal-lint-rule-sets';

  constructor(protected http: HttpClient) {}

  create(portalLintRuleSet: IPortalLintRuleSet): Observable<EntityResponseType> {
    return this.http.post<IPortalLintRuleSet>(this.resourceUrl, portalLintRuleSet, { observe: 'response' });
  }

  update(portalLintRuleSet: IPortalLintRuleSet): Observable<EntityResponseType> {
    return this.http.put<IPortalLintRuleSet>(this.resourceUrl, portalLintRuleSet, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPortalLintRuleSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPortalLintRuleSet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
