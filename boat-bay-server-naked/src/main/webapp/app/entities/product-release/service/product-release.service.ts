import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductRelease, getProductReleaseIdentifier } from '../product-release.model';

export type EntityResponseType = HttpResponse<IProductRelease>;
export type EntityArrayResponseType = HttpResponse<IProductRelease[]>;

@Injectable({ providedIn: 'root' })
export class ProductReleaseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-releases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productRelease: IProductRelease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productRelease);
    return this.http
      .post<IProductRelease>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productRelease: IProductRelease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productRelease);
    return this.http
      .put<IProductRelease>(`${this.resourceUrl}/${getProductReleaseIdentifier(productRelease) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productRelease: IProductRelease): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productRelease);
    return this.http
      .patch<IProductRelease>(`${this.resourceUrl}/${getProductReleaseIdentifier(productRelease) as number}`, copy, { observe: 'response' })
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

  addProductReleaseToCollectionIfMissing(
    productReleaseCollection: IProductRelease[],
    ...productReleasesToCheck: (IProductRelease | null | undefined)[]
  ): IProductRelease[] {
    const productReleases: IProductRelease[] = productReleasesToCheck.filter(isPresent);
    if (productReleases.length > 0) {
      const productReleaseCollectionIdentifiers = productReleaseCollection.map(
        productReleaseItem => getProductReleaseIdentifier(productReleaseItem)!
      );
      const productReleasesToAdd = productReleases.filter(productReleaseItem => {
        const productReleaseIdentifier = getProductReleaseIdentifier(productReleaseItem);
        if (productReleaseIdentifier == null || productReleaseCollectionIdentifiers.includes(productReleaseIdentifier)) {
          return false;
        }
        productReleaseCollectionIdentifiers.push(productReleaseIdentifier);
        return true;
      });
      return [...productReleasesToAdd, ...productReleaseCollection];
    }
    return productReleaseCollection;
  }

  protected convertDateFromClient(productRelease: IProductRelease): IProductRelease {
    return Object.assign({}, productRelease, {
      releaseDate: productRelease.releaseDate?.isValid() ? productRelease.releaseDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.releaseDate = res.body.releaseDate ? dayjs(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productRelease: IProductRelease) => {
        productRelease.releaseDate = productRelease.releaseDate ? dayjs(productRelease.releaseDate) : undefined;
      });
    }
    return res;
  }
}
