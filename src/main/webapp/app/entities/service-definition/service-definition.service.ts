import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';

type EntityResponseType = HttpResponse<IServiceDefinition>;
type EntityArrayResponseType = HttpResponse<IServiceDefinition[]>;

@Injectable({ providedIn: 'root' })
export class ServiceDefinitionService {
  public resourceUrl = SERVER_API_URL + 'api/service-definitions';

  constructor(protected http: HttpClient) {}

  create(serviceDefinition: IServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceDefinition);
    return this.http
      .post<IServiceDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(serviceDefinition: IServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceDefinition);
    return this.http
      .put<IServiceDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IServiceDefinition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IServiceDefinition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(serviceDefinition: IServiceDefinition): IServiceDefinition {
    const copy: IServiceDefinition = Object.assign({}, serviceDefinition, {
      createdOn: serviceDefinition.createdOn && serviceDefinition.createdOn.isValid() ? serviceDefinition.createdOn.toJSON() : undefined,
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
      res.body.forEach((serviceDefinition: IServiceDefinition) => {
        serviceDefinition.createdOn = serviceDefinition.createdOn ? moment(serviceDefinition.createdOn) : undefined;
      });
    }
    return res;
  }
}
