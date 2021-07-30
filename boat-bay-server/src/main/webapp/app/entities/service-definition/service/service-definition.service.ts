import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceDefinition, getServiceDefinitionIdentifier } from '../service-definition.model';

export type EntityResponseType = HttpResponse<IServiceDefinition>;
export type EntityArrayResponseType = HttpResponse<IServiceDefinition[]>;

@Injectable({ providedIn: 'root' })
export class ServiceDefinitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-definitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serviceDefinition: IServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceDefinition);
    return this.http
      .post<IServiceDefinition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(serviceDefinition: IServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceDefinition);
    return this.http
      .put<IServiceDefinition>(`${this.resourceUrl}/${getServiceDefinitionIdentifier(serviceDefinition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(serviceDefinition: IServiceDefinition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceDefinition);
    return this.http
      .patch<IServiceDefinition>(`${this.resourceUrl}/${getServiceDefinitionIdentifier(serviceDefinition) as number}`, copy, {
        observe: 'response',
      })
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

  addServiceDefinitionToCollectionIfMissing(
    serviceDefinitionCollection: IServiceDefinition[],
    ...serviceDefinitionsToCheck: (IServiceDefinition | null | undefined)[]
  ): IServiceDefinition[] {
    const serviceDefinitions: IServiceDefinition[] = serviceDefinitionsToCheck.filter(isPresent);
    if (serviceDefinitions.length > 0) {
      const serviceDefinitionCollectionIdentifiers = serviceDefinitionCollection.map(
        serviceDefinitionItem => getServiceDefinitionIdentifier(serviceDefinitionItem)!
      );
      const serviceDefinitionsToAdd = serviceDefinitions.filter(serviceDefinitionItem => {
        const serviceDefinitionIdentifier = getServiceDefinitionIdentifier(serviceDefinitionItem);
        if (serviceDefinitionIdentifier == null || serviceDefinitionCollectionIdentifiers.includes(serviceDefinitionIdentifier)) {
          return false;
        }
        serviceDefinitionCollectionIdentifiers.push(serviceDefinitionIdentifier);
        return true;
      });
      return [...serviceDefinitionsToAdd, ...serviceDefinitionCollection];
    }
    return serviceDefinitionCollection;
  }

  protected convertDateFromClient(serviceDefinition: IServiceDefinition): IServiceDefinition {
    return Object.assign({}, serviceDefinition, {
      createdOn: serviceDefinition.createdOn?.isValid() ? serviceDefinition.createdOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((serviceDefinition: IServiceDefinition) => {
        serviceDefinition.createdOn = serviceDefinition.createdOn ? dayjs(serviceDefinition.createdOn) : undefined;
      });
    }
    return res;
  }
}
