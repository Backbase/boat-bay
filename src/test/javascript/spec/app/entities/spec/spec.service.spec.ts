import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SpecService } from 'app/entities/spec/spec.service';
import { ISpec, Spec } from 'app/shared/model/spec.model';

describe('Service Tests', () => {
  describe('Spec Service', () => {
    let injector: TestBed;
    let service: SpecService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpec;
    let expectedResult: ISpec | ISpec[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SpecService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Spec(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA'
      );
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
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            openApi: 'BBBBBB',
            tagsCsv: 'BBBBBB',
            description: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            checksum: 'BBBBBB',
            filename: 'BBBBBB',
            valid: true,
            order: 1,
            parseError: 'BBBBBB',
            sourcePath: 'BBBBBB',
            sourceName: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            sourceCreatedBy: 'BBBBBB',
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedBy: 'BBBBBB',
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

      it('should return a list of Spec', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            openApi: 'BBBBBB',
            tagsCsv: 'BBBBBB',
            description: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            checksum: 'BBBBBB',
            filename: 'BBBBBB',
            valid: true,
            order: 1,
            parseError: 'BBBBBB',
            sourcePath: 'BBBBBB',
            sourceName: 'BBBBBB',
            sourceUrl: 'BBBBBB',
            sourceCreatedBy: 'BBBBBB',
            sourceCreatedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedOn: currentDate.format(DATE_TIME_FORMAT),
            sourceLastModifiedBy: 'BBBBBB',
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
