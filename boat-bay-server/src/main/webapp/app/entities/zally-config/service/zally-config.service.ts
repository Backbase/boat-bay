import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IZallyConfig, getZallyConfigIdentifier } from '../zally-config.model';

export type EntityResponseType = HttpResponse<IZallyConfig>;
export type EntityArrayResponseType = HttpResponse<IZallyConfig[]>;

@Injectable({ providedIn: 'root' })
export class ZallyConfigService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/zally-configs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(zallyConfig: IZallyConfig): Observable<EntityResponseType> {
    return this.http.post<IZallyConfig>(this.resourceUrl, zallyConfig, { observe: 'response' });
  }

  update(zallyConfig: IZallyConfig): Observable<EntityResponseType> {
    return this.http.put<IZallyConfig>(`${this.resourceUrl}/${getZallyConfigIdentifier(zallyConfig) as number}`, zallyConfig, {
      observe: 'response',
    });
  }

  partialUpdate(zallyConfig: IZallyConfig): Observable<EntityResponseType> {
    return this.http.patch<IZallyConfig>(`${this.resourceUrl}/${getZallyConfigIdentifier(zallyConfig) as number}`, zallyConfig, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IZallyConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IZallyConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addZallyConfigToCollectionIfMissing(
    zallyConfigCollection: IZallyConfig[],
    ...zallyConfigsToCheck: (IZallyConfig | null | undefined)[]
  ): IZallyConfig[] {
    const zallyConfigs: IZallyConfig[] = zallyConfigsToCheck.filter(isPresent);
    if (zallyConfigs.length > 0) {
      const zallyConfigCollectionIdentifiers = zallyConfigCollection.map(zallyConfigItem => getZallyConfigIdentifier(zallyConfigItem)!);
      const zallyConfigsToAdd = zallyConfigs.filter(zallyConfigItem => {
        const zallyConfigIdentifier = getZallyConfigIdentifier(zallyConfigItem);
        if (zallyConfigIdentifier == null || zallyConfigCollectionIdentifiers.includes(zallyConfigIdentifier)) {
          return false;
        }
        zallyConfigCollectionIdentifiers.push(zallyConfigIdentifier);
        return true;
      });
      return [...zallyConfigsToAdd, ...zallyConfigCollection];
    }
    return zallyConfigCollection;
  }
}
