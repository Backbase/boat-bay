import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Changes } from 'app/entities/enumerations/changes.model';
import { ISpec, Spec } from '../spec.model';

import { SpecService } from './spec.service';

describe('Service Tests', () => {
  describe('Spec Service', () => {
    let service: SpecService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpec;
    let expectedResult: ISpec | ISpec[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SpecService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
        name: 'AAAAAAA',
        version: 'AAAAAAA',
        title: 'AAAAAAA',
        icon: 'AAAAAAA',
        openApi: 'AAAAAAA',
        description: 'AAAAAAA',
        createdOn: currentDate,
        createdBy: 'AAAAAAA',
        checksum: 'AAAAAAA',
        filename: 'AAAAAAA',
        valid: false,
        order: 0,
        parseError: 'AAAAAAA',
        externalDocs: 'AAAAAAA',
        hide: false,
        grade: 'AAAAAAA',
        changes: Changes.INVALID_VERSION,
        sourcePath: 'AAAAAAA',
        sourceName: 'AAAAAAA',
        sourceUrl: 'AAAAAAA',
        sourceCreatedBy: 'AAAAAAA',
        sourceCreatedOn: currentDate,
        sourceLastModifiedOn: currentDate,
        sourceLastModifiedBy: 'AAAAAAA',
        mvnGroupId: 'AAAAAAA',
        mvnArtifactId: 'AAAAAAA',
        mvnVersion: 'AAAAAAA',
        mvnClassifier: 'AAAAAAA',
        mvnExtension: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Spec', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
            sourceCreatedOn: currentDate,
            sourceLastModifiedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new Spec()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Spec', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            icon: 'BBBBBB',
            openApi: 'BBBBBB',
            description: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            checksum: 'BBBBBB',
            filename: 'BBBBBB',
            valid: true,
            order: 1,
            parseError: 'BBBBBB',
            externalDocs: 'BBBBBB',
            hide: true,
            grade: 'BBBBBB',
            changes: 'BBBBBB',
            sourcePath: 'BBBBBB',
            sourceName: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            sourceCreatedBy: 'BBBBBB',
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedBy: 'BBBBBB',
            mvnGroupId: 'BBBBBB',
            mvnArtifactId: 'BBBBBB',
            mvnVersion: 'BBBBBB',
            mvnClassifier: 'BBBBBB',
            mvnExtension: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
            sourceCreatedOn: currentDate,
            sourceLastModifiedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Spec', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            icon: 'BBBBBB',
            description: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            checksum: 'BBBBBB',
            valid: true,
            externalDocs: 'BBBBBB',
            grade: 'BBBBBB',
            sourcePath: 'BBBBBB',
            sourceCreatedBy: 'BBBBBB',
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            mvnGroupId: 'BBBBBB',
            mvnArtifactId: 'BBBBBB',
            mvnVersion: 'BBBBBB',
          },
          new Spec()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdOn: currentDate,
            sourceCreatedOn: currentDate,
            sourceLastModifiedOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Spec', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            icon: 'BBBBBB',
            openApi: 'BBBBBB',
            description: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            checksum: 'BBBBBB',
            filename: 'BBBBBB',
            valid: true,
            order: 1,
            parseError: 'BBBBBB',
            externalDocs: 'BBBBBB',
            hide: true,
            grade: 'BBBBBB',
            changes: 'BBBBBB',
            sourcePath: 'BBBBBB',
            sourceName: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            sourceCreatedBy: 'BBBBBB',
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedBy: 'BBBBBB',
            mvnGroupId: 'BBBBBB',
            mvnArtifactId: 'BBBBBB',
            mvnVersion: 'BBBBBB',
            mvnClassifier: 'BBBBBB',
            mvnExtension: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
            sourceCreatedOn: currentDate,
            sourceLastModifiedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Spec', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSpecToCollectionIfMissing', () => {
        it('should add a Spec to an empty array', () => {
          const spec: ISpec = { id: 123 };
          expectedResult = service.addSpecToCollectionIfMissing([], spec);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(spec);
        });

        it('should not add a Spec to an array that contains it', () => {
          const spec: ISpec = { id: 123 };
          const specCollection: ISpec[] = [
            {
              ...spec,
            },
            { id: 456 },
          ];
          expectedResult = service.addSpecToCollectionIfMissing(specCollection, spec);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Spec to an array that doesn't contain it", () => {
          const spec: ISpec = { id: 123 };
          const specCollection: ISpec[] = [{ id: 456 }];
          expectedResult = service.addSpecToCollectionIfMissing(specCollection, spec);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(spec);
        });

        it('should add only unique Spec to an array', () => {
          const specArray: ISpec[] = [{ id: 123 }, { id: 456 }, { id: 48918 }];
          const specCollection: ISpec[] = [{ id: 123 }];
          expectedResult = service.addSpecToCollectionIfMissing(specCollection, ...specArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const spec: ISpec = { id: 123 };
          const spec2: ISpec = { id: 456 };
          expectedResult = service.addSpecToCollectionIfMissing([], spec, spec2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(spec);
          expect(expectedResult).toContain(spec2);
        });

        it('should accept null and undefined values', () => {
          const spec: ISpec = { id: 123 };
          expectedResult = service.addSpecToCollectionIfMissing([], null, spec, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(spec);
        });

        it('should return initial array if no Spec is added', () => {
          const specCollection: ISpec[] = [{ id: 123 }];
          expectedResult = service.addSpecToCollectionIfMissing(specCollection, undefined, null);
          expect(expectedResult).toEqual(specCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
