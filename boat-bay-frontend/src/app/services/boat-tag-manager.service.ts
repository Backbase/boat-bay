import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({ providedIn: 'root' })
export class BoatTagManagerService {

  public resourceUrl = '/api/boat/tags';

  constructor(protected http: HttpClient) {}

  hide(tag: String): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.resourceUrl}/hide`, tag, { observe: 'response' });
  }
}
