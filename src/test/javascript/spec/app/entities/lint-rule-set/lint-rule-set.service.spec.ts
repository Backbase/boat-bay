import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LintRuleSetService } from 'app/entities/lint-rule-set/lint-rule-set.service';
import { ILintRuleSet, LintRuleSet } from 'app/shared/model/lint-rule-set.model';

describe('Service Tests', () => {
  describe('LintRuleSet Service', () => {
    let injector: TestBed;
    let service: LintRuleSetService;
    let httpMock: HttpTestingController;
    let elemDefault: ILintRuleSet;
    let expectedResult: ILintRuleSet | ILintRuleSet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LintRuleSetService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LintRuleSet(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of LintRuleSet', () => {
        const returnedFromService = Object.assign(
          {
            ruleSetId: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            externalUrl: 'BBBBBB',
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
