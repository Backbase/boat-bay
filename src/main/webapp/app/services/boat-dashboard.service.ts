import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { PortalView } from 'app/models/dashboard/v1';

@Injectable({ providedIn: 'root' })
export class BoatDashboardService {
  public resourceUrl = SERVER_API_URL + 'api/boat';

  constructor(protected http: HttpClient) {}

  get(): Observable<PortalView> {
    return this.http.get<PortalView>(`${this.resourceUrl}/legacy-dashboard`);
  }
}
