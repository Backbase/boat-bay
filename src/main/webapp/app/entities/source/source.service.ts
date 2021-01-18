import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISource } from 'app/shared/model/source.model';

type EntityResponseType = HttpResponse<ISource>;
type EntityArrayResponseType = HttpResponse<ISource[]>;

@Injectable({ providedIn: 'root' })
export class SourceService {
  public resourceUrl = SERVER_API_URL + 'api/sources';

  constructor(protected http: HttpClient) {}

  create(source: ISource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(source);
    return this.http
      .post<ISource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(source: ISource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(source);
    return this.http
      .put<ISource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(source: ISource): ISource {
    const copy: ISource = Object.assign({}, source, {
      filterArtifactsCreatedSince:
        source.filterArtifactsCreatedSince && source.filterArtifactsCreatedSince.isValid()
          ? source.filterArtifactsCreatedSince.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.filterArtifactsCreatedSince = res.body.filterArtifactsCreatedSince
        ? moment(res.body.filterArtifactsCreatedSince)
        : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((source: ISource) => {
        source.filterArtifactsCreatedSince = source.filterArtifactsCreatedSince ? moment(source.filterArtifactsCreatedSince) : undefined;
      });
    }
    return res;
  }
}
