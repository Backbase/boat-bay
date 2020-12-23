import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LintRuleViolationService } from 'app/entities/lint-rule-violation/lint-rule-violation.service';
import { ILintRuleViolation, LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

describe('Service Tests', () => {
  describe('LintRuleViolation Service', () => {
    let injector: TestBed;
    let service: LintRuleViolationService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintRuleViolation;
    let expectedResult: ILintRuleViolation | ILintRuleViolation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LintRuleViolationService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LintRuleViolation(0, 'AAAAAAA', Severity.MUST, 0, 0, 0, 0, 'AAAAAAA');
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
            description: 'BBBBBB',
            severity: 'BBBBBB',
            lineStart: 1,
            lindEnd: 1,
            columnStart: 1,
            columnEnd: 1,
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

      it('should return a list of LintRuleViolation', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            severity: 'BBBBBB',
            lineStart: 1,
            lindEnd: 1,
            columnStart: 1,
            columnEnd: 1,
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
