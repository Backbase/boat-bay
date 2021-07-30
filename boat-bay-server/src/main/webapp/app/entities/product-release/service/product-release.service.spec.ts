import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductRelease, ProductRelease } from '../product-release.model';

import { ProductReleaseService } from './product-release.service';

describe('Service Tests', () => {
  describe('ProductRelease Service', () => {
    let service: ProductReleaseService;
    let httpMock: HttpTestingController;
    let elemDefault: IProductRelease;
    let expectedResult: IProductRelease | IProductRelease[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProductReleaseService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        version: 'AAAAAAA',
        releaseDate: currentDate,
        hide: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ProductRelease', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ProductRelease()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ProductRelease', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            hide: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ProductRelease', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            version: 'BBBBBB',
          },
          new ProductRelease()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ProductRelease', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            hide: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ProductRelease', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProductReleaseToCollectionIfMissing', () => {
        it('should add a ProductRelease to an empty array', () => {
          const productRelease: IProductRelease = { id: 123 };
          expectedResult = service.addProductReleaseToCollectionIfMissing([], productRelease);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(productRelease);
        });

        it('should not add a ProductRelease to an array that contains it', () => {
          const productRelease: IProductRelease = { id: 123 };
          const productReleaseCollection: IProductRelease[] = [
            {
              ...productRelease,
            },
            { id: 456 },
          ];
          expectedResult = service.addProductReleaseToCollectionIfMissing(productReleaseCollection, productRelease);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ProductRelease to an array that doesn't contain it", () => {
          const productRelease: IProductRelease = { id: 123 };
          const productReleaseCollection: IProductRelease[] = [{ id: 456 }];
          expectedResult = service.addProductReleaseToCollectionIfMissing(productReleaseCollection, productRelease);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(productRelease);
        });

        it('should add only unique ProductRelease to an array', () => {
          const productReleaseArray: IProductRelease[] = [{ id: 123 }, { id: 456 }, { id: 43449 }];
          const productReleaseCollection: IProductRelease[] = [{ id: 123 }];
          expectedResult = service.addProductReleaseToCollectionIfMissing(productReleaseCollection, ...productReleaseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const productRelease: IProductRelease = { id: 123 };
          const productRelease2: IProductRelease = { id: 456 };
          expectedResult = service.addProductReleaseToCollectionIfMissing([], productRelease, productRelease2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(productRelease);
          expect(expectedResult).toContain(productRelease2);
        });

        it('should accept null and undefined values', () => {
          const productRelease: IProductRelease = { id: 123 };
          expectedResult = service.addProductReleaseToCollectionIfMissing([], null, productRelease, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(productRelease);
        });

        it('should return initial array if no ProductRelease is added', () => {
          const productReleaseCollection: IProductRelease[] = [{ id: 123 }];
          expectedResult = service.addProductReleaseToCollectionIfMissing(productReleaseCollection, undefined, null);
          expect(expectedResult).toEqual(productReleaseCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
