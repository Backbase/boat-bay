import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleSetDetailComponent } from 'app/entities/lint-rule-set/lint-rule-set-detail.component';
import { LintRuleSet } from 'app/shared/model/lint-rule-set.model';

describe('Component Tests', () => {
  describe('LintRuleSet Management Detail Component', () => {
    let comp: LintRuleSetDetailComponent;
    let fixture: ComponentFixture<LintRuleSetDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ lintRuleSet: new LintRuleSet(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleSetDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LintRuleSetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintRuleSetDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load lintRuleSet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lintRuleSet).toEqual(jasmine.objectContaining({ id: 123 }));
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
