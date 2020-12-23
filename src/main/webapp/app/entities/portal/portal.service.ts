import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPortal } from 'app/shared/model/portal.model';

type EntityResponseType = HttpResponse<IPortal>;
type EntityArrayResponseType = HttpResponse<IPortal[]>;

@Injectable({ providedIn: 'root' })
export class PortalService {
  public resourceUrl = SERVER_API_URL + 'api/portals';

  constructor(protected http: HttpClient) {}

  create(portal: IPortal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portal);
    return this.http
      .post<IPortal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(portal: IPortal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portal);
    return this.http
      .put<IPortal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPortal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPortal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(portal: IPortal): IPortal {
    const copy: IPortal = Object.assign({}, portal, {
      createdOn: portal.createdOn && portal.createdOn.isValid() ? portal.createdOn.toJSON() : undefined,
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
      res.body.forEach((portal: IPortal) => {
        portal.createdOn = portal.createdOn ? moment(portal.createdOn) : undefined;
      });
    }
    return res;
  }
}
