import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISpec } from 'app/shared/model/spec.model';

type EntityResponseType = HttpResponse<ISpec>;
type EntityArrayResponseType = HttpResponse<ISpec[]>;

@Injectable({ providedIn: 'root' })
export class SpecService {
  public resourceUrl = SERVER_API_URL + 'api/specs';

  constructor(protected http: HttpClient) {}

  create(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .post<ISpec>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .put<ISpec>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISpec>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISpec[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(spec: ISpec): ISpec {
    const copy: ISpec = Object.assign({}, spec, {
      createdOn: spec.createdOn && spec.createdOn.isValid() ? spec.createdOn.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((spec: ISpec) => {
        spec.createdOn = spec.createdOn ? moment(spec.createdOn) : undefined;
      });
    }
    return res;
  }
}
