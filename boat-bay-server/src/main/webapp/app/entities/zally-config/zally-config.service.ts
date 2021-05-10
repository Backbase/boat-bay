import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IZallyConfig } from 'app/shared/model/zally-config.model';

type EntityResponseType = HttpResponse<IZallyConfig>;
type EntityArrayResponseType = HttpResponse<IZallyConfig[]>;

@Injectable({ providedIn: 'root' })
export class ZallyConfigService {
  public resourceUrl = SERVER_API_URL + 'api/zally-configs';

  constructor(protected http: HttpClient) {}

  create(zallyConfig: IZallyConfig): Observable<EntityResponseType> {
    return this.http.post<IZallyConfig>(this.resourceUrl, zallyConfig, { observe: 'response' });
  }

  update(zallyConfig: IZallyConfig): Observable<EntityResponseType> {
    return this.http.put<IZallyConfig>(this.resourceUrl, zallyConfig, { observe: 'response' });
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
}
