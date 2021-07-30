import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LintRuleDetailComponent } from './lint-rule-detail.component';

describe('Component Tests', () => {
  describe('LintRule Management Detail Component', () => {
    let comp: LintRuleDetailComponent;
    let fixture: ComponentFixture<LintRuleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LintRuleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lintRule: { id: 123 } }) },
          },
        ],
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
        expect(comp.lintRule).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
