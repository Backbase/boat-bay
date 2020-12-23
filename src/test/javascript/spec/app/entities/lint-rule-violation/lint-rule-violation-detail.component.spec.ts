import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleViolationDetailComponent } from 'app/entities/lint-rule-violation/lint-rule-violation-detail.component';
import { LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

describe('Component Tests', () => {
  describe('LintRuleViolation Management Detail Component', () => {
    let comp: LintRuleViolationDetailComponent;
    let fixture: ComponentFixture<LintRuleViolationDetailComponent>;
    const route = ({ data: of({ lintRuleViolation: new LintRuleViolation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleViolationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LintRuleViolationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintRuleViolationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lintRuleViolation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lintRuleViolation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
