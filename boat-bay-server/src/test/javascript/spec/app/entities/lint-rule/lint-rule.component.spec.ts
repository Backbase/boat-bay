import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleComponent } from 'app/entities/lint-rule/lint-rule.component';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { LintRule } from 'app/shared/model/lint-rule.model';

describe('Component Tests', () => {
  describe('LintRule Management Component', () => {
    let comp: LintRuleComponent;
    let fixture: ComponentFixture<LintRuleComponent>;
    let service: LintRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleComponent],
      })
        .overrideTemplate(LintRuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LintRule(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lintRules && comp.lintRules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
