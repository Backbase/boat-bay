import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILintReport, getLintReportIdentifier } from '../lint-report.model';

export type EntityResponseType = HttpResponse<ILintReport>;
export type EntityArrayResponseType = HttpResponse<ILintReport[]>;

@Injectable({ providedIn: 'root' })
export class LintReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lint-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lintReport: ILintReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lintReport);
    return this.http
      .post<ILintReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lintReport: ILintReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lintReport);
    return this.http
      .put<ILintReport>(`${this.resourceUrl}/${getLintReportIdentifier(lintReport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lintReport: ILintReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lintReport);
    return this.http
      .patch<ILintReport>(`${this.resourceUrl}/${getLintReportIdentifier(lintReport) as number}`, copy, { observe: 'response' })
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

  addLintReportToCollectionIfMissing(
    lintReportCollection: ILintReport[],
    ...lintReportsToCheck: (ILintReport | null | undefined)[]
  ): ILintReport[] {
    const lintReports: ILintReport[] = lintReportsToCheck.filter(isPresent);
    if (lintReports.length > 0) {
      const lintReportCollectionIdentifiers = lintReportCollection.map(lintReportItem => getLintReportIdentifier(lintReportItem)!);
      const lintReportsToAdd = lintReports.filter(lintReportItem => {
        const lintReportIdentifier = getLintReportIdentifier(lintReportItem);
        if (lintReportIdentifier == null || lintReportCollectionIdentifiers.includes(lintReportIdentifier)) {
          return false;
        }
        lintReportCollectionIdentifiers.push(lintReportIdentifier);
        return true;
      });
      return [...lintReportsToAdd, ...lintReportCollection];
    }
    return lintReportCollection;
  }

  protected convertDateFromClient(lintReport: ILintReport): ILintReport {
    return Object.assign({}, lintReport, {
      lintedOn: lintReport.lintedOn?.isValid() ? lintReport.lintedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lintedOn = res.body.lintedOn ? dayjs(res.body.lintedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lintReport: ILintReport) => {
        lintReport.lintedOn = lintReport.lintedOn ? dayjs(lintReport.lintedOn) : undefined;
      });
    }
    return res;
  }
}
