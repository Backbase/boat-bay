import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct, getProductIdentifier } from '../product.model';

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .post<IProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .put<IProduct>(`${this.resourceUrl}/${getProductIdentifier(product) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .patch<IProduct>(`${this.resourceUrl}/${getProductIdentifier(product) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductToCollectionIfMissing(productCollection: IProduct[], ...productsToCheck: (IProduct | null | undefined)[]): IProduct[] {
    const products: IProduct[] = productsToCheck.filter(isPresent);
    if (products.length > 0) {
      const productCollectionIdentifiers = productCollection.map(productItem => getProductIdentifier(productItem)!);
      const productsToAdd = products.filter(productItem => {
        const productIdentifier = getProductIdentifier(productItem);
        if (productIdentifier == null || productCollectionIdentifiers.includes(productIdentifier)) {
          return false;
        }
        productCollectionIdentifiers.push(productIdentifier);
        return true;
      });
      return [...productsToAdd, ...productCollection];
    }
    return productCollection;
  }

  protected convertDateFromClient(product: IProduct): IProduct {
    return Object.assign({}, product, {
      createdOn: product.createdOn?.isValid() ? product.createdOn.toJSON() : undefined,
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
      res.body.forEach((product: IProduct) => {
        product.createdOn = product.createdOn ? dayjs(product.createdOn) : undefined;
      });
    }
    return res;
  }
}
