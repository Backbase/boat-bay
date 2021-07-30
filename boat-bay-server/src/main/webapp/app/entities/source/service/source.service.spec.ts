import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { SourceType } from 'app/entities/enumerations/source-type.model';
import { ISource, Source } from '../source.model';

import { SourceService } from './source.service';

describe('Service Tests', () => {
  describe('Source Service', () => {
    let service: SourceService;
    let httpMock: HttpTestingController;
    let elemDefault: ISource;
    let expectedResult: ISource | ISource[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SourceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        key: 'AAAAAAA',
        type: SourceType.JFROG,
        baseUrl: 'AAAAAAA',
        active: false,
        filterArtifactsName: 'AAAAAAA',
        filterArtifactsCreatedSince: currentDate,
        username: 'AAAAAAA',
        password: 'AAAAAAA',
        cronExpression: 'AAAAAAA',
        runOnStartup: false,
        specFilterSpEL: 'AAAAAAA',
        capabilityKeySpEL: 'AAAAAAA',
        capabilityNameSpEL: 'AAAAAAA',
        serviceKeySpEL: 'AAAAAAA',
        serviceNameSpEL: 'AAAAAAA',
        specKeySpEL: 'AAAAAAA',
        versionSpEL: 'AAAAAAA',
        productReleaseNameSpEL: 'AAAAAAA',
        productReleaseVersionSpEL: 'AAAAAAA',
        productReleaseKeySpEL: 'AAAAAAA',
        itemLimit: 0,
        overwriteChanges: false,
        options: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Source', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.create(new Source()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Source', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            key: 'BBBBBB',
            type: 'BBBBBB',
            baseUrl: 'BBBBBB',
            active: true,
            filterArtifactsName: 'BBBBBB',
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            runOnStartup: true,
            specFilterSpEL: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
            specKeySpEL: 'BBBBBB',
            versionSpEL: 'BBBBBB',
            productReleaseNameSpEL: 'BBBBBB',
            productReleaseVersionSpEL: 'BBBBBB',
            productReleaseKeySpEL: 'BBBBBB',
            itemLimit: 1,
            overwriteChanges: true,
            options: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Source', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            baseUrl: 'BBBBBB',
            filterArtifactsName: 'BBBBBB',
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
            cronExpression: 'BBBBBB',
            specFilterSpEL: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            specKeySpEL: 'BBBBBB',
            productReleaseVersionSpEL: 'BBBBBB',
            productReleaseKeySpEL: 'BBBBBB',
            itemLimit: 1,
            overwriteChanges: true,
            options: 'BBBBBB',
          },
          new Source()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Source', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            key: 'BBBBBB',
            type: 'BBBBBB',
            baseUrl: 'BBBBBB',
            active: true,
            filterArtifactsName: 'BBBBBB',
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            runOnStartup: true,
            specFilterSpEL: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
            specKeySpEL: 'BBBBBB',
            versionSpEL: 'BBBBBB',
            productReleaseNameSpEL: 'BBBBBB',
            productReleaseVersionSpEL: 'BBBBBB',
            productReleaseKeySpEL: 'BBBBBB',
            itemLimit: 1,
            overwriteChanges: true,
            options: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Source', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSourceToCollectionIfMissing', () => {
        it('should add a Source to an empty array', () => {
          const source: ISource = { id: 123 };
          expectedResult = service.addSourceToCollectionIfMissing([], source);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(source);
        });

        it('should not add a Source to an array that contains it', () => {
          const source: ISource = { id: 123 };
          const sourceCollection: ISource[] = [
            {
              ...source,
            },
            { id: 456 },
          ];
          expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, source);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Source to an array that doesn't contain it", () => {
          const source: ISource = { id: 123 };
          const sourceCollection: ISource[] = [{ id: 456 }];
          expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, source);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(source);
        });

        it('should add only unique Source to an array', () => {
          const sourceArray: ISource[] = [{ id: 123 }, { id: 456 }, { id: 88605 }];
          const sourceCollection: ISource[] = [{ id: 123 }];
          expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, ...sourceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const source: ISource = { id: 123 };
          const source2: ISource = { id: 456 };
          expectedResult = service.addSourceToCollectionIfMissing([], source, source2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(source);
          expect(expectedResult).toContain(source2);
        });

        it('should accept null and undefined values', () => {
          const source: ISource = { id: 123 };
          expectedResult = service.addSourceToCollectionIfMissing([], null, source, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(source);
        });

        it('should return initial array if no Source is added', () => {
          const sourceCollection: ISource[] = [{ id: 123 }];
          expectedResult = service.addSourceToCollectionIfMissing(sourceCollection, undefined, null);
          expect(expectedResult).toEqual(sourceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
