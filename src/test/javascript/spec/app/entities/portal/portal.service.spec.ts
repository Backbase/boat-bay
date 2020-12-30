import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PortalService } from 'app/entities/portal/portal.service';
import { IPortal, Portal } from 'app/shared/model/portal.model';

describe('Service Tests', () => {
  describe('Portal Service', () => {
    let injector: TestBed;
    let service: PortalService;
    let httpMock: HttpTestingController;
    let elemDefault: IPortal;
    let expectedResult: IPortal | IPortal[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PortalService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Portal(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA'
      );
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
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            navTitle: 'BBBBBB',
            logoUrl: 'BBBBBB',
            logoLink: 'BBBBBB',
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
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

      it('should return a list of Portal', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            name: 'BBBBBB',
            version: 'BBBBBB',
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            navTitle: 'BBBBBB',
            logoUrl: 'BBBBBB',
            logoLink: 'BBBBBB',
            content: 'BBBBBB',
            createdOn: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
