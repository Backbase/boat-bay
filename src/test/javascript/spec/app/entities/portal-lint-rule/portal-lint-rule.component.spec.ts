import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleComponent } from 'app/entities/portal-lint-rule/portal-lint-rule.component';
import { PortalLintRuleService } from 'app/entities/portal-lint-rule/portal-lint-rule.service';
import { PortalLintRule } from 'app/shared/model/portal-lint-rule.model';

describe('Component Tests', () => {
  describe('PortalLintRule Management Component', () => {
    let comp: PortalLintRuleComponent;
    let fixture: ComponentFixture<PortalLintRuleComponent>;
    let service: PortalLintRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleComponent],
      })
        .overrideTemplate(PortalLintRuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalLintRuleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PortalLintRule(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.portalLintRules && comp.portalLintRules[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
