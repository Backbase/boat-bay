import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LintReport } from '../models/lint-report';

@Injectable({
  providedIn: 'root',
})
export class BoatQuayService {
  public resourceUrl = SERVER_API_URL + 'api/v1/lint';

  constructor(protected http: HttpClient) {}

  getAll(): Observable<HttpResponse<LintReport[]>> {
    return this.http.get<LintReport[]>(`${this.resourceUrl}/report/`, { observe: 'response' });
  }

  getReport(id: number): Observable<HttpResponse<LintReport>> {
    return this.http.get<LintReport>(`${this.resourceUrl}/report/spec/${id}`, { observe: 'response' });
  }
}
