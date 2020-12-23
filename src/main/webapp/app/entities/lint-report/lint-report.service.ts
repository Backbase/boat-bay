import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILintReport } from 'app/shared/model/lint-report.model';

type EntityResponseType = HttpResponse<ILintReport>;
type EntityArrayResponseType = HttpResponse<ILintReport[]>;

@Injectable({ providedIn: 'root' })
export class LintReportService {
  public resourceUrl = SERVER_API_URL + 'api/lint-reports';

  constructor(protected http: HttpClient) {}

  create(lintReport: ILintReport): Observable<EntityResponseType> {
    return this.http.post<ILintReport>(this.resourceUrl, lintReport, { observe: 'response' });
  }

  update(lintReport: ILintReport): Observable<EntityResponseType> {
    return this.http.put<ILintReport>(this.resourceUrl, lintReport, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILintReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILintReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
