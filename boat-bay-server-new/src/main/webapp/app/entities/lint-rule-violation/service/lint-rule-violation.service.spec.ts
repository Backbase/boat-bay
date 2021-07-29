import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Severity } from 'app/entities/enumerations/severity.model';
import { ILintRuleViolation, LintRuleViolation } from '../lint-rule-violation.model';

import { LintRuleViolationService } from './lint-rule-violation.service';

describe('Service Tests', () => {
  describe('LintRuleViolation Service', () => {
    let service: LintRuleViolationService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintRuleViolation;
    let expectedResult: ILintRuleViolation | ILintRuleViolation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LintRuleViolationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        url: 'AAAAAAA',
        severity: Severity.MUST,
        lineStart: 0,
        lineEnd: 0,
        jsonPointer: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LintRuleViolation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LintRuleViolation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LintRuleViolation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            severity: 'BBBBBB',
            lineStart: 1,
            lineEnd: 1,
            jsonPointer: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LintRuleViolation', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            severity: 'BBBBBB',
            jsonPointer: 'BBBBBB',
          },
          new LintRuleViolation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LintRuleViolation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            severity: 'BBBBBB',
            lineStart: 1,
            lineEnd: 1,
            jsonPointer: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LintRuleViolation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLintRuleViolationToCollectionIfMissing', () => {
        it('should add a LintRuleViolation to an empty array', () => {
          const lintRuleViolation: ILintRuleViolation = { id: 123 };
          expectedResult = service.addLintRuleViolationToCollectionIfMissing([], lintRuleViolation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintRuleViolation);
        });

        it('should not add a LintRuleViolation to an array that contains it', () => {
          const lintRuleViolation: ILintRuleViolation = { id: 123 };
          const lintRuleViolationCollection: ILintRuleViolation[] = [
            {
              ...lintRuleViolation,
            },
            { id: 456 },
          ];
          expectedResult = service.addLintRuleViolationToCollectionIfMissing(lintRuleViolationCollection, lintRuleViolation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LintRuleViolation to an array that doesn't contain it", () => {
          const lintRuleViolation: ILintRuleViolation = { id: 123 };
          const lintRuleViolationCollection: ILintRuleViolation[] = [{ id: 456 }];
          expectedResult = service.addLintRuleViolationToCollectionIfMissing(lintRuleViolationCollection, lintRuleViolation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintRuleViolation);
        });

        it('should add only unique LintRuleViolation to an array', () => {
          const lintRuleViolationArray: ILintRuleViolation[] = [{ id: 123 }, { id: 456 }, { id: 46692 }];
          const lintRuleViolationCollection: ILintRuleViolation[] = [{ id: 123 }];
          expectedResult = service.addLintRuleViolationToCollectionIfMissing(lintRuleViolationCollection, ...lintRuleViolationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lintRuleViolation: ILintRuleViolation = { id: 123 };
          const lintRuleViolation2: ILintRuleViolation = { id: 456 };
          expectedResult = service.addLintRuleViolationToCollectionIfMissing([], lintRuleViolation, lintRuleViolation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintRuleViolation);
          expect(expectedResult).toContain(lintRuleViolation2);
        });

        it('should accept null and undefined values', () => {
          const lintRuleViolation: ILintRuleViolation = { id: 123 };
          expectedResult = service.addLintRuleViolationToCollectionIfMissing([], null, lintRuleViolation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintRuleViolation);
        });

        it('should return initial array if no LintRuleViolation is added', () => {
          const lintRuleViolationCollection: ILintRuleViolation[] = [{ id: 123 }];
          expectedResult = service.addLintRuleViolationToCollectionIfMissing(lintRuleViolationCollection, undefined, null);
          expect(expectedResult).toEqual(lintRuleViolationCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
