import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISpecType } from 'app/shared/model/spec-type.model';

type EntityResponseType = HttpResponse<ISpecType>;
type EntityArrayResponseType = HttpResponse<ISpecType[]>;

@Injectable({ providedIn: 'root' })
export class SpecTypeService {
  public resourceUrl = SERVER_API_URL + 'api/spec-types';

  constructor(protected http: HttpClient) {}

  create(specType: ISpecType): Observable<EntityResponseType> {
    return this.http.post<ISpecType>(this.resourceUrl, specType, { observe: 'response' });
  }

  update(specType: ISpecType): Observable<EntityResponseType> {
    return this.http.put<ISpecType>(this.resourceUrl, specType, { observe: 'response' });
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
}
