import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleConfigComponent } from 'app/entities/portal-lint-rule-config/portal-lint-rule-config.component';
import { PortalLintRuleConfigService } from 'app/entities/portal-lint-rule-config/portal-lint-rule-config.service';
import { PortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';

describe('Component Tests', () => {
  describe('PortalLintRuleConfig Management Component', () => {
    let comp: PortalLintRuleConfigComponent;
    let fixture: ComponentFixture<PortalLintRuleConfigComponent>;
    let service: PortalLintRuleConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleConfigComponent],
      })
        .overrideTemplate(PortalLintRuleConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalLintRuleConfigComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleConfigService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PortalLintRuleConfig(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.portalLintRuleConfigs && comp.portalLintRuleConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
