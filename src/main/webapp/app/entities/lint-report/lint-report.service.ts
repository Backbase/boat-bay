import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

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
    const copy = this.convertDateFromClient(lintReport);
    return this.http
      .post<ILintReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lintReport: ILintReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lintReport);
    return this.http
      .put<ILintReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILintReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILintReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(lintReport: ILintReport): ILintReport {
    const copy: ILintReport = Object.assign({}, lintReport, {
      lintedOn: lintReport.lintedOn && lintReport.lintedOn.isValid() ? lintReport.lintedOn.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lintedOn = res.body.lintedOn ? moment(res.body.lintedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lintReport: ILintReport) => {
        lintReport.lintedOn = lintReport.lintedOn ? moment(lintReport.lintedOn) : undefined;
      });
    }
    return res;
  }
}
