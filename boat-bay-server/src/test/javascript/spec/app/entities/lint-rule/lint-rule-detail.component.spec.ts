import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleDetailComponent } from 'app/entities/lint-rule/lint-rule-detail.component';
import { LintRule } from 'app/shared/model/lint-rule.model';

describe('Component Tests', () => {
  describe('LintRule Management Detail Component', () => {
    let comp: LintRuleDetailComponent;
    let fixture: ComponentFixture<LintRuleDetailComponent>;
    const route = ({ data: of({ lintRule: new LintRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LintRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lintRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lintRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
