import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleSetDetailComponent } from 'app/entities/lint-rule-set/lint-rule-set-detail.component';
import { LintRuleSet } from 'app/shared/model/lint-rule-set.model';

describe('Component Tests', () => {
  describe('LintRuleSet Management Detail Component', () => {
    let comp: LintRuleSetDetailComponent;
    let fixture: ComponentFixture<LintRuleSetDetailComponent>;
    const route = ({ data: of({ lintRuleSet: new LintRuleSet(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleSetDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LintRuleSetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintRuleSetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lintRuleSet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lintRuleSet).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
