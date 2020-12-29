import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleSetDetailComponent } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set-detail.component';
import { PortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

describe('Component Tests', () => {
  describe('PortalLintRuleSet Management Detail Component', () => {
    let comp: PortalLintRuleSetDetailComponent;
    let fixture: ComponentFixture<PortalLintRuleSetDetailComponent>;
    const route = ({ data: of({ portalLintRuleSet: new PortalLintRuleSet(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleSetDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PortalLintRuleSetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PortalLintRuleSetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load portalLintRuleSet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.portalLintRuleSet).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
