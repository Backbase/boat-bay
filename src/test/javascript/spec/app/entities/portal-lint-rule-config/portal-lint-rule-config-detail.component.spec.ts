import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleConfigDetailComponent } from 'app/entities/portal-lint-rule-config/portal-lint-rule-config-detail.component';
import { PortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';

describe('Component Tests', () => {
  describe('PortalLintRuleConfig Management Detail Component', () => {
    let comp: PortalLintRuleConfigDetailComponent;
    let fixture: ComponentFixture<PortalLintRuleConfigDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ portalLintRuleConfig: new PortalLintRuleConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PortalLintRuleConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PortalLintRuleConfigDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load portalLintRuleConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.portalLintRuleConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
