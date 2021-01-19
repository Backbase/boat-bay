import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityDetailComponent } from 'app/entities/capability/capability-detail.component';
import { Capability } from 'app/shared/model/capability.model';

describe('Component Tests', () => {
  describe('Capability Management Detail Component', () => {
    let comp: CapabilityDetailComponent;
    let fixture: ComponentFixture<CapabilityDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ capability: new Capability(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CapabilityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CapabilityDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load capability on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.capability).toEqual(jasmine.objectContaining({ id: 123 }));
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
