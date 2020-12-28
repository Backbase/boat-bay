import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SourceSpecService } from 'app/entities/source-spec/source-spec.service';
import { ISourceSpec, SourceSpec } from 'app/shared/model/source-spec.model';

describe('Service Tests', () => {
  describe('SourceSpec Service', () => {
    let injector: TestBed;
    let service: SourceSpecService;
    let httpMock: HttpTestingController;
    let elemDefault: ISourceSpec;
    let expectedResult: ISourceSpec | ISourceSpec[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SourceSpecService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SourceSpec(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            scannedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SourceSpec', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            scannedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            scannedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new SourceSpec()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SourceSpec', () => {
        const returnedFromService = Object.assign(
          {
            url: 'BBBBBB',
            type: 'BBBBBB',
            name: 'BBBBBB',
            scannedOn: currentDate.format(DATE_TIME_FORMAT),
            downloaded: true,
            checksum: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            scannedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SourceSpec', () => {
        const returnedFromService = Object.assign(
          {
            url: 'BBBBBB',
            type: 'BBBBBB',
            name: 'BBBBBB',
            scannedOn: currentDate.format(DATE_TIME_FORMAT),
            downloaded: true,
            checksum: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            scannedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SourceSpec', () => {
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
