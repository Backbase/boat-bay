import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPortal, Portal } from '../portal.model';

import { PortalService } from './portal.service';

describe('Service Tests', () => {
  describe('Portal Service', () => {
    let service: PortalService;
    let httpMock: HttpTestingController;
    let elemDefault: IPortal;
    let expectedResult: IPortal | IPortal[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PortalService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        subTitle: 'AAAAAAA',
        logoUrl: 'AAAAAAA',
        logoLink: 'AAAAAAA',
        content: 'AAAAAAA',
        createdOn: currentDate,
        createdBy: 'AAAAAAA',
        hide: false,
        linted: false,
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

      it('should create a Portal', () => {
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

        service.create(new Portal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Portal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            subTitle: 'BBBBBB',
            logoUrl: 'BBBBBB',
            logoLink: 'BBBBBB',
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
            linted: true,
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

      it('should partial update a Portal', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
            content: 'BBBBBB',
            createdBy: 'BBBBBB',
            linted: true,
          },
          new Portal()
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

      it('should return a list of Portal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            subTitle: 'BBBBBB',
            logoUrl: 'BBBBBB',
            logoLink: 'BBBBBB',
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
            linted: true,
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

      it('should delete a Portal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPortalToCollectionIfMissing', () => {
        it('should add a Portal to an empty array', () => {
          const portal: IPortal = { id: 123 };
          expectedResult = service.addPortalToCollectionIfMissing([], portal);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(portal);
        });

        it('should not add a Portal to an array that contains it', () => {
          const portal: IPortal = { id: 123 };
          const portalCollection: IPortal[] = [
            {
              ...portal,
            },
            { id: 456 },
          ];
          expectedResult = service.addPortalToCollectionIfMissing(portalCollection, portal);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Portal to an array that doesn't contain it", () => {
          const portal: IPortal = { id: 123 };
          const portalCollection: IPortal[] = [{ id: 456 }];
          expectedResult = service.addPortalToCollectionIfMissing(portalCollection, portal);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(portal);
        });

        it('should add only unique Portal to an array', () => {
          const portalArray: IPortal[] = [{ id: 123 }, { id: 456 }, { id: 62833 }];
          const portalCollection: IPortal[] = [{ id: 123 }];
          expectedResult = service.addPortalToCollectionIfMissing(portalCollection, ...portalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const portal: IPortal = { id: 123 };
          const portal2: IPortal = { id: 456 };
          expectedResult = service.addPortalToCollectionIfMissing([], portal, portal2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(portal);
          expect(expectedResult).toContain(portal2);
        });

        it('should accept null and undefined values', () => {
          const portal: IPortal = { id: 123 };
          expectedResult = service.addPortalToCollectionIfMissing([], null, portal, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(portal);
        });

        it('should return initial array if no Portal is added', () => {
          const portalCollection: IPortal[] = [{ id: 123 }];
          expectedResult = service.addPortalToCollectionIfMissing(portalCollection, undefined, null);
          expect(expectedResult).toEqual(portalCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
