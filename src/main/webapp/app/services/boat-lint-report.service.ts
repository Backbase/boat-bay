import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BoatLintReport } from 'app/models/lint-report';

@Injectable({
  providedIn: 'root',
})
export class BoatLintReportService {
  public resourceUrl = SERVER_API_URL + 'api/v1/lint';

  constructor(protected http: HttpClient) {}

  getAll(): Observable<HttpResponse<BoatLintReport[]>> {
    return this.http.get<BoatLintReport[]>(`${this.resourceUrl}/report/`, { observe: 'response' });
  }

  getReport(id: number): Observable<HttpResponse<BoatLintReport>> {
    return this.http.get<BoatLintReport>(`${this.resourceUrl}/report/spec/${id}`, { observe: 'response' });
  }

  postLintProduct(productId: number): Observable<void> {
    return this.http.post<void>(`${this.resourceUrl}/product`, productId);
  }

  postLintCapability(id: number): Observable<void> {
    return this.http.post<void>(`${this.resourceUrl}/capability`, id);
  }
}
