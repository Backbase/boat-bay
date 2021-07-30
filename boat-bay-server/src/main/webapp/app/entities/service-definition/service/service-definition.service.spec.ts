import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServiceDefinition, ServiceDefinition } from '../service-definition.model';

import { ServiceDefinitionService } from './service-definition.service';

describe('Service Tests', () => {
  describe('ServiceDefinition Service', () => {
    let service: ServiceDefinitionService;
    let httpMock: HttpTestingController;
    let elemDefault: IServiceDefinition;
    let expectedResult: IServiceDefinition | IServiceDefinition[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ServiceDefinitionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        order: 0,
        subTitle: 'AAAAAAA',
        description: 'AAAAAAA',
        icon: 'AAAAAAA',
        color: 'AAAAAAA',
        createdOn: currentDate,
        createdBy: 'AAAAAAA',
        hide: false,
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

      it('should create a ServiceDefinition', () => {
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

        service.create(new ServiceDefinition()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ServiceDefinition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            order: 1,
            subTitle: 'BBBBBB',
            description: 'BBBBBB',
            icon: 'BBBBBB',
            color: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
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

      it('should partial update a ServiceDefinition', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            icon: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
          },
          new ServiceDefinition()
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

      it('should return a list of ServiceDefinition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            order: 1,
            subTitle: 'BBBBBB',
            description: 'BBBBBB',
            icon: 'BBBBBB',
            color: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            hide: true,
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

      it('should delete a ServiceDefinition', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addServiceDefinitionToCollectionIfMissing', () => {
        it('should add a ServiceDefinition to an empty array', () => {
          const serviceDefinition: IServiceDefinition = { id: 123 };
          expectedResult = service.addServiceDefinitionToCollectionIfMissing([], serviceDefinition);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceDefinition);
        });

        it('should not add a ServiceDefinition to an array that contains it', () => {
          const serviceDefinition: IServiceDefinition = { id: 123 };
          const serviceDefinitionCollection: IServiceDefinition[] = [
            {
              ...serviceDefinition,
            },
            { id: 456 },
          ];
          expectedResult = service.addServiceDefinitionToCollectionIfMissing(serviceDefinitionCollection, serviceDefinition);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ServiceDefinition to an array that doesn't contain it", () => {
          const serviceDefinition: IServiceDefinition = { id: 123 };
          const serviceDefinitionCollection: IServiceDefinition[] = [{ id: 456 }];
          expectedResult = service.addServiceDefinitionToCollectionIfMissing(serviceDefinitionCollection, serviceDefinition);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceDefinition);
        });

        it('should add only unique ServiceDefinition to an array', () => {
          const serviceDefinitionArray: IServiceDefinition[] = [{ id: 123 }, { id: 456 }, { id: 53959 }];
          const serviceDefinitionCollection: IServiceDefinition[] = [{ id: 123 }];
          expectedResult = service.addServiceDefinitionToCollectionIfMissing(serviceDefinitionCollection, ...serviceDefinitionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const serviceDefinition: IServiceDefinition = { id: 123 };
          const serviceDefinition2: IServiceDefinition = { id: 456 };
          expectedResult = service.addServiceDefinitionToCollectionIfMissing([], serviceDefinition, serviceDefinition2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceDefinition);
          expect(expectedResult).toContain(serviceDefinition2);
        });

        it('should accept null and undefined values', () => {
          const serviceDefinition: IServiceDefinition = { id: 123 };
          expectedResult = service.addServiceDefinitionToCollectionIfMissing([], null, serviceDefinition, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceDefinition);
        });

        it('should return initial array if no ServiceDefinition is added', () => {
          const serviceDefinitionCollection: IServiceDefinition[] = [{ id: 123 }];
          expectedResult = service.addServiceDefinitionToCollectionIfMissing(serviceDefinitionCollection, undefined, null);
          expect(expectedResult).toEqual(serviceDefinitionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
