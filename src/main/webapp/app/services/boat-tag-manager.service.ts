import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class BoatTagManagerService {
  public resourceUrl = SERVER_API_URL + 'api/boat/tags';

  constructor(protected http: HttpClient) {}

  hide(tag: String): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.resourceUrl}/hide`, tag, { observe: 'response' });
  }
}
