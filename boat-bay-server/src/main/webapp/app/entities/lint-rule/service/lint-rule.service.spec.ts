import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Severity } from 'app/entities/enumerations/severity.model';
import { ILintRule, LintRule } from '../lint-rule.model';

import { LintRuleService } from './lint-rule.service';

describe('Service Tests', () => {
  describe('LintRule Service', () => {
    let service: LintRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintRule;
    let expectedResult: ILintRule | ILintRule[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LintRuleService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        ruleId: 'AAAAAAA',
        title: 'AAAAAAA',
        ruleSet: 'AAAAAAA',
        summary: 'AAAAAAA',
        severity: Severity.MUST,
        description: 'AAAAAAA',
        externalUrl: 'AAAAAAA',
        enabled: false,
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

      it('should create a LintRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LintRule()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LintRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ruleId: 'BBBBBB',
            title: 'BBBBBB',
            ruleSet: 'BBBBBB',
            summary: 'BBBBBB',
            severity: 'BBBBBB',
            description: 'BBBBBB',
            externalUrl: 'BBBBBB',
            enabled: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LintRule', () => {
        const patchObject = Object.assign(
          {
            ruleId: 'BBBBBB',
            title: 'BBBBBB',
            summary: 'BBBBBB',
            severity: 'BBBBBB',
          },
          new LintRule()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LintRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ruleId: 'BBBBBB',
            title: 'BBBBBB',
            ruleSet: 'BBBBBB',
            summary: 'BBBBBB',
            severity: 'BBBBBB',
            description: 'BBBBBB',
            externalUrl: 'BBBBBB',
            enabled: true,
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

      it('should delete a LintRule', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLintRuleToCollectionIfMissing', () => {
        it('should add a LintRule to an empty array', () => {
          const lintRule: ILintRule = { id: 123 };
          expectedResult = service.addLintRuleToCollectionIfMissing([], lintRule);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintRule);
        });

        it('should not add a LintRule to an array that contains it', () => {
          const lintRule: ILintRule = { id: 123 };
          const lintRuleCollection: ILintRule[] = [
            {
              ...lintRule,
            },
            { id: 456 },
          ];
          expectedResult = service.addLintRuleToCollectionIfMissing(lintRuleCollection, lintRule);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LintRule to an array that doesn't contain it", () => {
          const lintRule: ILintRule = { id: 123 };
          const lintRuleCollection: ILintRule[] = [{ id: 456 }];
          expectedResult = service.addLintRuleToCollectionIfMissing(lintRuleCollection, lintRule);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintRule);
        });

        it('should add only unique LintRule to an array', () => {
          const lintRuleArray: ILintRule[] = [{ id: 123 }, { id: 456 }, { id: 18456 }];
          const lintRuleCollection: ILintRule[] = [{ id: 123 }];
          expectedResult = service.addLintRuleToCollectionIfMissing(lintRuleCollection, ...lintRuleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lintRule: ILintRule = { id: 123 };
          const lintRule2: ILintRule = { id: 456 };
          expectedResult = service.addLintRuleToCollectionIfMissing([], lintRule, lintRule2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lintRule);
          expect(expectedResult).toContain(lintRule2);
        });

        it('should accept null and undefined values', () => {
          const lintRule: ILintRule = { id: 123 };
          expectedResult = service.addLintRuleToCollectionIfMissing([], null, lintRule, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lintRule);
        });

        it('should return initial array if no LintRule is added', () => {
          const lintRuleCollection: ILintRule[] = [{ id: 123 }];
          expectedResult = service.addLintRuleToCollectionIfMissing(lintRuleCollection, undefined, null);
          expect(expectedResult).toEqual(lintRuleCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
