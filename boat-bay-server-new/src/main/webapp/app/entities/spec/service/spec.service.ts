import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpec, getSpecIdentifier } from '../spec.model';

export type EntityResponseType = HttpResponse<ISpec>;
export type EntityArrayResponseType = HttpResponse<ISpec[]>;

@Injectable({ providedIn: 'root' })
export class SpecService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/specs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .post<ISpec>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .put<ISpec>(`${this.resourceUrl}/${getSpecIdentifier(spec) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(spec: ISpec): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spec);
    return this.http
      .patch<ISpec>(`${this.resourceUrl}/${getSpecIdentifier(spec) as number}`, copy, { observe: 'response' })
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

  addSpecToCollectionIfMissing(specCollection: ISpec[], ...specsToCheck: (ISpec | null | undefined)[]): ISpec[] {
    const specs: ISpec[] = specsToCheck.filter(isPresent);
    if (specs.length > 0) {
      const specCollectionIdentifiers = specCollection.map(specItem => getSpecIdentifier(specItem)!);
      const specsToAdd = specs.filter(specItem => {
        const specIdentifier = getSpecIdentifier(specItem);
        if (specIdentifier == null || specCollectionIdentifiers.includes(specIdentifier)) {
          return false;
        }
        specCollectionIdentifiers.push(specIdentifier);
        return true;
      });
      return [...specsToAdd, ...specCollection];
    }
    return specCollection;
  }

  protected convertDateFromClient(spec: ISpec): ISpec {
    return Object.assign({}, spec, {
      createdOn: spec.createdOn?.isValid() ? spec.createdOn.toJSON() : undefined,
      sourceCreatedOn: spec.sourceCreatedOn?.isValid() ? spec.sourceCreatedOn.toJSON() : undefined,
      sourceLastModifiedOn: spec.sourceLastModifiedOn?.isValid() ? spec.sourceLastModifiedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
      res.body.sourceCreatedOn = res.body.sourceCreatedOn ? dayjs(res.body.sourceCreatedOn) : undefined;
      res.body.sourceLastModifiedOn = res.body.sourceLastModifiedOn ? dayjs(res.body.sourceLastModifiedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((spec: ISpec) => {
        spec.createdOn = spec.createdOn ? dayjs(spec.createdOn) : undefined;
        spec.sourceCreatedOn = spec.sourceCreatedOn ? dayjs(spec.sourceCreatedOn) : undefined;
        spec.sourceLastModifiedOn = spec.sourceLastModifiedOn ? dayjs(spec.sourceLastModifiedOn) : undefined;
      });
    }
    return res;
  }
}
