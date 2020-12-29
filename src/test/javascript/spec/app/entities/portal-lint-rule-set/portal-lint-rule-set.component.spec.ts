import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleSetComponent } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set.component';
import { PortalLintRuleSetService } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set.service';
import { PortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

describe('Component Tests', () => {
  describe('PortalLintRuleSet Management Component', () => {
    let comp: PortalLintRuleSetComponent;
    let fixture: ComponentFixture<PortalLintRuleSetComponent>;
    let service: PortalLintRuleSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleSetComponent],
      })
        .overrideTemplate(PortalLintRuleSetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalLintRuleSetComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleSetService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PortalLintRuleSet(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.portalLintRuleSets && comp.portalLintRuleSets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
