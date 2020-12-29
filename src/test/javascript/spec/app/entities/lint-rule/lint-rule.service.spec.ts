import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { ILintRule, LintRule } from 'app/shared/model/lint-rule.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

describe('Service Tests', () => {
  describe('LintRule Service', () => {
    let injector: TestBed;
    let service: LintRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintRule;
    let expectedResult: ILintRule | ILintRule[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LintRuleService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LintRule(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', Severity.MUST, 'AAAAAAA', 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of LintRule', () => {
        const returnedFromService = Object.assign(
          {
            ruleId: 'BBBBBB',
            title: 'BBBBBB',
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
