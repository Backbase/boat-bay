import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';

type EntityResponseType = HttpResponse<ICapabilityServiceDefinition>;
type EntityArrayResponseType = HttpResponse<ICapabilityServiceDefinition[]>;

@Injectable({ providedIn: 'root' })
export class CapabilityServiceDefinitionService {
  public resourceUrl = SERVER_API_URL + 'api/capability-service-definitions';

  constructor(protected http: HttpClient) {}

  create(capabilityServiceDefinition: ICapabilityServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capabilityServiceDefinition);
    return this.http
      .post<ICapabilityServiceDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(capabilityServiceDefinition: ICapabilityServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capabilityServiceDefinition);
    return this.http
      .put<ICapabilityServiceDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICapabilityServiceDefinition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICapabilityServiceDefinition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(capabilityServiceDefinition: ICapabilityServiceDefinition): ICapabilityServiceDefinition {
    const copy: ICapabilityServiceDefinition = Object.assign({}, capabilityServiceDefinition, {
      createdOn:
        capabilityServiceDefinition.createdOn && capabilityServiceDefinition.createdOn.isValid()
          ? capabilityServiceDefinition.createdOn.toJSON()
          : undefined,
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
      res.body.forEach((capabilityServiceDefinition: ICapabilityServiceDefinition) => {
        capabilityServiceDefinition.createdOn = capabilityServiceDefinition.createdOn
          ? moment(capabilityServiceDefinition.createdOn)
          : undefined;
      });
    }
    return res;
  }
}
