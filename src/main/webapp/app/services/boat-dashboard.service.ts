import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { LegacyPortalView } from 'app/models/dashboard/v1';
import { BoatPortal } from 'app/models/dashboard/boat-portal';

@Injectable({ providedIn: 'root' })
export class BoatDashboardService {
  public resourceUrl = SERVER_API_URL + 'api/boat';

  constructor(protected http: HttpClient) {}

  getLegacyPortalView(): Observable<LegacyPortalView> {
    return this.http.get<LegacyPortalView>(`${this.resourceUrl}/legacy-dashboard`);
  }

  getBoatPortalView(): Observable<BoatPortal[]> {
    return this.http.get<BoatPortal[]>(`${this.resourceUrl}/dashboard`);
  }
}
