import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISource } from 'app/shared/model/source.model';

type EntityResponseType = HttpResponse<ISource>;
type EntityArrayResponseType = HttpResponse<ISource[]>;

@Injectable({ providedIn: 'root' })
export class SourceService {
  public resourceUrl = SERVER_API_URL + 'api/sources';

  constructor(protected http: HttpClient) {}

  create(source: ISource): Observable<EntityResponseType> {
    return this.http.post<ISource>(this.resourceUrl, source, { observe: 'response' });
  }

  update(source: ISource): Observable<EntityResponseType> {
    return this.http.put<ISource>(this.resourceUrl, source, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
