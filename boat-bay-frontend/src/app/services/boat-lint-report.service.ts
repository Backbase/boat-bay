import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BoatLintReport } from "../models/";

@Injectable({
  providedIn: 'root',
})
export class BoatLintReportService {
  public resourceUrl = '/api/boat';

  constructor(protected http: HttpClient) {}

  getAll(): Observable<HttpResponse<BoatLintReport[]>> {
    return this.http.get<BoatLintReport[]>(`${this.resourceUrl}/report/`, { observe: 'response' });
  }

  getReport(portalKey: string, productKey: string, id: number): Observable<HttpResponse<BoatLintReport>> {
    return this.http.get<BoatLintReport>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/specs/${id}/lint-report`, { observe: 'response' });
  }

  postLintProduct(productId: number): Observable<void> {
    return this.http.post<void>(`${this.resourceUrl}/product`, productId);
  }

  postLintCapability(id: number): Observable<void> {
    return this.http.post<void>(`${this.resourceUrl}/capability`, id);
  }
}
