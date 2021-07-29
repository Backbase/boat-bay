import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, Product } from '../product.model';

import { ProductService } from './product.service';

describe('Service Tests', () => {
  describe('Product Service', () => {
    let service: ProductService;
    let httpMock: HttpTestingController;
    let elemDefault: IProduct;
    let expectedResult: IProduct | IProduct[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProductService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        order: 0,
        content: 'AAAAAAA',
        createdOn: currentDate,
        createdBy: 'AAAAAAA',
        hide: false,
        jiraProjectId: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Product', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
          },
          returnedFromService
        );

        service.create(new Product()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Product', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            order: 1,
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
            jiraProjectId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Product', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            order: 1,
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
            jiraProjectId: 'BBBBBB',
          },
          new Product()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Product', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            order: 1,
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
            jiraProjectId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Product', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProductToCollectionIfMissing', () => {
        it('should add a Product to an empty array', () => {
          const product: IProduct = { id: 123 };
          expectedResult = service.addProductToCollectionIfMissing([], product);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(product);
        });

        it('should not add a Product to an array that contains it', () => {
          const product: IProduct = { id: 123 };
          const productCollection: IProduct[] = [
            {
              ...product,
            },
            { id: 456 },
          ];
          expectedResult = service.addProductToCollectionIfMissing(productCollection, product);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Product to an array that doesn't contain it", () => {
          const product: IProduct = { id: 123 };
          const productCollection: IProduct[] = [{ id: 456 }];
          expectedResult = service.addProductToCollectionIfMissing(productCollection, product);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(product);
        });

        it('should add only unique Product to an array', () => {
          const productArray: IProduct[] = [{ id: 123 }, { id: 456 }, { id: 18708 }];
          const productCollection: IProduct[] = [{ id: 123 }];
          expectedResult = service.addProductToCollectionIfMissing(productCollection, ...productArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const product: IProduct = { id: 123 };
          const product2: IProduct = { id: 456 };
          expectedResult = service.addProductToCollectionIfMissing([], product, product2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(product);
          expect(expectedResult).toContain(product2);
        });

        it('should accept null and undefined values', () => {
          const product: IProduct = { id: 123 };
          expectedResult = service.addProductToCollectionIfMissing([], null, product, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(product);
        });

        it('should return initial array if no Product is added', () => {
          const productCollection: IProduct[] = [{ id: 123 }];
          expectedResult = service.addProductToCollectionIfMissing(productCollection, undefined, null);
          expect(expectedResult).toEqual(productCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
