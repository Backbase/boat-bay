import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { LegacyPortalView } from 'app/models/dashboard/v1';
import { BoatDashboard } from 'app/models/dashboard/boat-dashboard';
import { BoatProductDashboard } from 'app/models/dashboard/boat-product-dashboard';

@Injectable({ providedIn: 'root' })
export class BoatDashboardService {
  public resourceUrl = SERVER_API_URL + 'api/boat';

  constructor(protected http: HttpClient) {}

  getLegacyPortalView(): Observable<LegacyPortalView> {
    return this.http.get<LegacyPortalView>(`${this.resourceUrl}/legacy-dashboard`);
  }

  getBoatPortalView(): Observable<BoatDashboard[]> {
    return this.http.get<BoatDashboard[]>(`${this.resourceUrl}/dashboard`);
  }

  getBoatProductDashboardView(portalKey: string, productKey: string): Observable<HttpResponse<BoatProductDashboard>> {
    return this.http.get<BoatProductDashboard>(`${this.resourceUrl}/dashboard/${portalKey}/${productKey}`, { observe: 'response' });
  }
}
