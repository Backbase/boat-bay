import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleDetailComponent } from 'app/entities/portal-lint-rule/portal-lint-rule-detail.component';
import { PortalLintRule } from 'app/shared/model/portal-lint-rule.model';

describe('Component Tests', () => {
  describe('PortalLintRule Management Detail Component', () => {
    let comp: PortalLintRuleDetailComponent;
    let fixture: ComponentFixture<PortalLintRuleDetailComponent>;
    const route = ({ data: of({ portalLintRule: new PortalLintRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PortalLintRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PortalLintRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load portalLintRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.portalLintRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
