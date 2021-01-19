import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProductRelease } from 'app/shared/model/product-release.model';

type EntityResponseType = HttpResponse<IProductRelease>;
type EntityArrayResponseType = HttpResponse<IProductRelease[]>;

@Injectable({ providedIn: 'root' })
export class ProductReleaseService {
  public resourceUrl = SERVER_API_URL + 'api/product-releases';

  constructor(protected http: HttpClient) {}

  create(productRelease: IProductRelease): Observable<EntityResponseType> {
    return this.http.post<IProductRelease>(this.resourceUrl, productRelease, { observe: 'response' });
  }

  update(productRelease: IProductRelease): Observable<EntityResponseType> {
    return this.http.put<IProductRelease>(this.resourceUrl, productRelease, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductRelease>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductRelease[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
