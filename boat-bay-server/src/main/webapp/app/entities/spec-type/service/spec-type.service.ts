import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpecType, getSpecTypeIdentifier } from '../spec-type.model';

export type EntityResponseType = HttpResponse<ISpecType>;
export type EntityArrayResponseType = HttpResponse<ISpecType[]>;

@Injectable({ providedIn: 'root' })
export class SpecTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spec-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(specType: ISpecType): Observable<EntityResponseType> {
    return this.http.post<ISpecType>(this.resourceUrl, specType, { observe: 'response' });
  }

  update(specType: ISpecType): Observable<EntityResponseType> {
    return this.http.put<ISpecType>(`${this.resourceUrl}/${getSpecTypeIdentifier(specType) as number}`, specType, { observe: 'response' });
  }

  partialUpdate(specType: ISpecType): Observable<EntityResponseType> {
    return this.http.patch<ISpecType>(`${this.resourceUrl}/${getSpecTypeIdentifier(specType) as number}`, specType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpecType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpecType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpecTypeToCollectionIfMissing(specTypeCollection: ISpecType[], ...specTypesToCheck: (ISpecType | null | undefined)[]): ISpecType[] {
    const specTypes: ISpecType[] = specTypesToCheck.filter(isPresent);
    if (specTypes.length > 0) {
      const specTypeCollectionIdentifiers = specTypeCollection.map(specTypeItem => getSpecTypeIdentifier(specTypeItem)!);
      const specTypesToAdd = specTypes.filter(specTypeItem => {
        const specTypeIdentifier = getSpecTypeIdentifier(specTypeItem);
        if (specTypeIdentifier == null || specTypeCollectionIdentifiers.includes(specTypeIdentifier)) {
          return false;
        }
        specTypeCollectionIdentifiers.push(specTypeIdentifier);
        return true;
      });
      return [...specTypesToAdd, ...specTypeCollection];
    }
    return specTypeCollection;
  }
}
