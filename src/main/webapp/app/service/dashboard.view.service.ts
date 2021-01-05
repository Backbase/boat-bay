import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { PortalView } from 'app/models';

@Injectable({ providedIn: 'root' })
export class DashboardViewService {
  public resourceUrl = SERVER_API_URL + 'api/view/dashboard';

  constructor(protected http: HttpClient) {}

  get(): Observable<PortalView> {
    return this.http.get<PortalView>(this.resourceUrl);
  }
}
