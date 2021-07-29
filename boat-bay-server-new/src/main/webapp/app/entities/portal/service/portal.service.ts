import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPortal, getPortalIdentifier } from '../portal.model';

export type EntityResponseType = HttpResponse<IPortal>;
export type EntityArrayResponseType = HttpResponse<IPortal[]>;

@Injectable({ providedIn: 'root' })
export class PortalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/portals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(portal: IPortal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portal);
    return this.http
      .post<IPortal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(portal: IPortal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portal);
    return this.http
      .put<IPortal>(`${this.resourceUrl}/${getPortalIdentifier(portal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(portal: IPortal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(portal);
    return this.http
      .patch<IPortal>(`${this.resourceUrl}/${getPortalIdentifier(portal) as number}`, copy, { observe: 'response' })
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

  addPortalToCollectionIfMissing(portalCollection: IPortal[], ...portalsToCheck: (IPortal | null | undefined)[]): IPortal[] {
    const portals: IPortal[] = portalsToCheck.filter(isPresent);
    if (portals.length > 0) {
      const portalCollectionIdentifiers = portalCollection.map(portalItem => getPortalIdentifier(portalItem)!);
      const portalsToAdd = portals.filter(portalItem => {
        const portalIdentifier = getPortalIdentifier(portalItem);
        if (portalIdentifier == null || portalCollectionIdentifiers.includes(portalIdentifier)) {
          return false;
        }
        portalCollectionIdentifiers.push(portalIdentifier);
        return true;
      });
      return [...portalsToAdd, ...portalCollection];
    }
    return portalCollection;
  }

  protected convertDateFromClient(portal: IPortal): IPortal {
    return Object.assign({}, portal, {
      createdOn: portal.createdOn?.isValid() ? portal.createdOn.toJSON() : undefined,
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
      res.body.forEach((portal: IPortal) => {
        portal.createdOn = portal.createdOn ? dayjs(portal.createdOn) : undefined;
      });
    }
    return res;
  }
}
