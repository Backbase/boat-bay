import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { ZallyConfigDetailComponent } from 'app/entities/zally-config/zally-config-detail.component';
import { ZallyConfig } from 'app/shared/model/zally-config.model';

describe('Component Tests', () => {
  describe('ZallyConfig Management Detail Component', () => {
    let comp: ZallyConfigDetailComponent;
    let fixture: ComponentFixture<ZallyConfigDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ zallyConfig: new ZallyConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ZallyConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ZallyConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ZallyConfigDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load zallyConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.zallyConfig).toEqual(jasmine.objectContaining({ id: 123 }));
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
