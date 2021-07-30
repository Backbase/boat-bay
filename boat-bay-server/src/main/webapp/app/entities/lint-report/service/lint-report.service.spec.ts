import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILintReport, LintReport } from '../lint-report.model';

import { LintReportService } from './lint-report.service';

describe('Service Tests', () => {
  describe('LintReport Service', () => {
    let service: LintReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintReport;
    let expectedResult: ILintReport | ILintReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LintReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        grade: 'AAAAAAA',
        passed: false,
        lintedOn: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lintedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LintReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lintedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lintedOn: currentDate,
          },
          returnedFromService
        );

        service.create(new LintReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LintReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            grade: 'BBBBBB',
            passed: true,
            lintedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lintedOn: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LintReport', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            passed: true,
          },
          new LintReport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            lintedOn: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LintReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            grade: 'BBBBBB',
            passed: true,
            lintedOn: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lintedOn: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LintReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLintReportToCollectionIfMissing', () => {
        it('should add a LintReport to an empty array', () => {
          const lintReport: ILintReport = { id: 123 };
          expectedResult = service.addLintReportToCollectionIfMissing([], lintReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintReport);
        });

        it('should not add a LintReport to an array that contains it', () => {
          const lintReport: ILintReport = { id: 123 };
          const lintReportCollection: ILintReport[] = [
            {
              ...lintReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addLintReportToCollectionIfMissing(lintReportCollection, lintReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LintReport to an array that doesn't contain it", () => {
          const lintReport: ILintReport = { id: 123 };
          const lintReportCollection: ILintReport[] = [{ id: 456 }];
          expectedResult = service.addLintReportToCollectionIfMissing(lintReportCollection, lintReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintReport);
        });

        it('should add only unique LintReport to an array', () => {
          const lintReportArray: ILintReport[] = [{ id: 123 }, { id: 456 }, { id: 824 }];
          const lintReportCollection: ILintReport[] = [{ id: 123 }];
          expectedResult = service.addLintReportToCollectionIfMissing(lintReportCollection, ...lintReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lintReport: ILintReport = { id: 123 };
          const lintReport2: ILintReport = { id: 456 };
          expectedResult = service.addLintReportToCollectionIfMissing([], lintReport, lintReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintReport);
          expect(expectedResult).toContain(lintReport2);
        });

        it('should accept null and undefined values', () => {
          const lintReport: ILintReport = { id: 123 };
          expectedResult = service.addLintReportToCollectionIfMissing([], null, lintReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintReport);
        });

        it('should return initial array if no LintReport is added', () => {
          const lintReportCollection: ILintReport[] = [{ id: 123 }];
          expectedResult = service.addLintReportToCollectionIfMissing(lintReportCollection, undefined, null);
          expect(expectedResult).toEqual(lintReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
