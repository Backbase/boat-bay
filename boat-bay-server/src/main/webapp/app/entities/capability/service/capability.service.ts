import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICapability, getCapabilityIdentifier } from '../capability.model';

export type EntityResponseType = HttpResponse<ICapability>;
export type EntityArrayResponseType = HttpResponse<ICapability[]>;

@Injectable({ providedIn: 'root' })
export class CapabilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/capabilities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(capability: ICapability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capability);
    return this.http
      .post<ICapability>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(capability: ICapability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capability);
    return this.http
      .put<ICapability>(`${this.resourceUrl}/${getCapabilityIdentifier(capability) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(capability: ICapability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(capability);
    return this.http
      .patch<ICapability>(`${this.resourceUrl}/${getCapabilityIdentifier(capability) as number}`, copy, { observe: 'response' })
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

  addCapabilityToCollectionIfMissing(
    capabilityCollection: ICapability[],
    ...capabilitiesToCheck: (ICapability | null | undefined)[]
  ): ICapability[] {
    const capabilities: ICapability[] = capabilitiesToCheck.filter(isPresent);
    if (capabilities.length > 0) {
      const capabilityCollectionIdentifiers = capabilityCollection.map(capabilityItem => getCapabilityIdentifier(capabilityItem)!);
      const capabilitiesToAdd = capabilities.filter(capabilityItem => {
        const capabilityIdentifier = getCapabilityIdentifier(capabilityItem);
        if (capabilityIdentifier == null || capabilityCollectionIdentifiers.includes(capabilityIdentifier)) {
          return false;
        }
        capabilityCollectionIdentifiers.push(capabilityIdentifier);
        return true;
      });
      return [...capabilitiesToAdd, ...capabilityCollection];
    }
    return capabilityCollection;
  }

  protected convertDateFromClient(capability: ICapability): ICapability {
    return Object.assign({}, capability, {
      createdOn: capability.createdOn?.isValid() ? capability.createdOn.toJSON() : undefined,
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
      res.body.forEach((capability: ICapability) => {
        capability.createdOn = capability.createdOn ? dayjs(capability.createdOn) : undefined;
      });
    }
    return res;
  }
}
