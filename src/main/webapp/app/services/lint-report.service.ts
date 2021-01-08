import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LintReport } from '../models/lint-report';

type EntityResponseType = HttpResponse<LintReport>;

@Injectable({
  providedIn: 'root',
})
export class LintReportService {
  public resourceUrl = SERVER_API_URL + 'api/v1/lint';

  constructor(protected http: HttpClient) {}

  get(id: number): Observable<EntityResponseType> {
    return this.http.get<LintReport>(`${this.resourceUrl}/report/spec/${id}`, { observe: 'response' });
  }
}
