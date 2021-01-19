import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICapability } from 'app/shared/model/capability.model';

type EntityResponseType = HttpResponse<ICapability>;
type EntityArrayResponseType = HttpResponse<ICapability[]>;

@Injectable({ providedIn: 'root' })
export class CapabilityService {
  public resourceUrl = SERVER_API_URL + 'api/capabilities';

  constructor(protected http: HttpClient) {}

  create(capability: ICapability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capability);
    return this.http
      .post<ICapability>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(capability: ICapability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capability);
    return this.http
      .put<ICapability>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICapability>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICapability[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(capability: ICapability): ICapability {
    const copy: ICapability = Object.assign({}, capability, {
      createdOn: capability.createdOn && capability.createdOn.isValid() ? capability.createdOn.toJSON() : undefined,
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
      res.body.forEach((capability: ICapability) => {
        capability.createdOn = capability.createdOn ? moment(capability.createdOn) : undefined;
      });
    }
    return res;
  }
}
