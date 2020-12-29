import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';

type EntityResponseType = HttpResponse<IPortalLintRuleConfig>;
type EntityArrayResponseType = HttpResponse<IPortalLintRuleConfig[]>;

@Injectable({ providedIn: 'root' })
export class PortalLintRuleConfigService {
  public resourceUrl = SERVER_API_URL + 'api/portal-lint-rule-configs';

  constructor(protected http: HttpClient) {}

  create(portalLintRuleConfig: IPortalLintRuleConfig): Observable<EntityResponseType> {
    return this.http.post<IPortalLintRuleConfig>(this.resourceUrl, portalLintRuleConfig, { observe: 'response' });
  }

  update(portalLintRuleConfig: IPortalLintRuleConfig): Observable<EntityResponseType> {
    return this.http.put<IPortalLintRuleConfig>(this.resourceUrl, portalLintRuleConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPortalLintRuleConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPortalLintRuleConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
