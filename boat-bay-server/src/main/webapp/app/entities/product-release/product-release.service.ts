import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

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
    const copy = this.convertDateFromClient(productRelease);
    return this.http
      .post<IProductRelease>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productRelease: IProductRelease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productRelease);
    return this.http
      .put<IProductRelease>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductRelease>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductRelease[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(productRelease: IProductRelease): IProductRelease {
    const copy: IProductRelease = Object.assign({}, productRelease, {
      releaseDate: productRelease.releaseDate && productRelease.releaseDate.isValid() ? productRelease.releaseDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.releaseDate = res.body.releaseDate ? moment(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productRelease: IProductRelease) => {
        productRelease.releaseDate = productRelease.releaseDate ? moment(productRelease.releaseDate) : undefined;
      });
    }
    return res;
  }
}
