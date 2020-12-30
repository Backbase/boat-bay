import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDashboard } from 'app/shared/model/dashboard.model';

type EntityResponseType = HttpResponse<IDashboard>;
type EntityArrayResponseType = HttpResponse<IDashboard[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  public resourceUrl = SERVER_API_URL + 'api/dashboards';

  constructor(protected http: HttpClient) {}

  create(dashboard: IDashboard): Observable<EntityResponseType> {
    return this.http.post<IDashboard>(this.resourceUrl, dashboard, { observe: 'response' });
  }

  update(dashboard: IDashboard): Observable<EntityResponseType> {
    return this.http.put<IDashboard>(this.resourceUrl, dashboard, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDashboard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDashboard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
