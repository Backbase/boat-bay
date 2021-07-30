import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDashboard, getDashboardIdentifier } from '../dashboard.model';

export type EntityResponseType = HttpResponse<IDashboard>;
export type EntityArrayResponseType = HttpResponse<IDashboard[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dashboard: IDashboard): Observable<EntityResponseType> {
    return this.http.post<IDashboard>(this.resourceUrl, dashboard, { observe: 'response' });
  }

  update(dashboard: IDashboard): Observable<EntityResponseType> {
    return this.http.put<IDashboard>(`${this.resourceUrl}/${getDashboardIdentifier(dashboard) as number}`, dashboard, {
      observe: 'response',
    });
  }

  partialUpdate(dashboard: IDashboard): Observable<EntityResponseType> {
    return this.http.patch<IDashboard>(`${this.resourceUrl}/${getDashboardIdentifier(dashboard) as number}`, dashboard, {
      observe: 'response',
    });
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

  addDashboardToCollectionIfMissing(
    dashboardCollection: IDashboard[],
    ...dashboardsToCheck: (IDashboard | null | undefined)[]
  ): IDashboard[] {
    const dashboards: IDashboard[] = dashboardsToCheck.filter(isPresent);
    if (dashboards.length > 0) {
      const dashboardCollectionIdentifiers = dashboardCollection.map(dashboardItem => getDashboardIdentifier(dashboardItem)!);
      const dashboardsToAdd = dashboards.filter(dashboardItem => {
        const dashboardIdentifier = getDashboardIdentifier(dashboardItem);
        if (dashboardIdentifier == null || dashboardCollectionIdentifiers.includes(dashboardIdentifier)) {
          return false;
        }
        dashboardCollectionIdentifiers.push(dashboardIdentifier);
        return true;
      });
      return [...dashboardsToAdd, ...dashboardCollection];
    }
    return dashboardCollection;
  }
}
