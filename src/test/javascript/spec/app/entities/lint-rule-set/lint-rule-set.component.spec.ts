import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleSetComponent } from 'app/entities/lint-rule-set/lint-rule-set.component';
import { LintRuleSetService } from 'app/entities/lint-rule-set/lint-rule-set.service';
import { LintRuleSet } from 'app/shared/model/lint-rule-set.model';

describe('Component Tests', () => {
  describe('LintRuleSet Management Component', () => {
    let comp: LintRuleSetComponent;
    let fixture: ComponentFixture<LintRuleSetComponent>;
    let service: LintRuleSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleSetComponent],
      })
        .overrideTemplate(LintRuleSetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleSetComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleSetService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LintRuleSet(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lintRuleSets && comp.lintRuleSets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
