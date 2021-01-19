import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleViolationComponent } from 'app/entities/lint-rule-violation/lint-rule-violation.component';
import { LintRuleViolationService } from 'app/entities/lint-rule-violation/lint-rule-violation.service';
import { LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

describe('Component Tests', () => {
  describe('LintRuleViolation Management Component', () => {
    let comp: LintRuleViolationComponent;
    let fixture: ComponentFixture<LintRuleViolationComponent>;
    let service: LintRuleViolationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleViolationComponent],
      })
        .overrideTemplate(LintRuleViolationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleViolationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleViolationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LintRuleViolation(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lintRuleViolations && comp.lintRuleViolations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
