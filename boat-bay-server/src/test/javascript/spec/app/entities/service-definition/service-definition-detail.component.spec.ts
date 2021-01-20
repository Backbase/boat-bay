import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { ServiceDefinitionDetailComponent } from 'app/entities/service-definition/service-definition-detail.component';
import { ServiceDefinition } from 'app/shared/model/service-definition.model';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Detail Component', () => {
    let comp: ServiceDefinitionDetailComponent;
    let fixture: ComponentFixture<ServiceDefinitionDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ serviceDefinition: new ServiceDefinition(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ServiceDefinitionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ServiceDefinitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServiceDefinitionDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load serviceDefinition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serviceDefinition).toEqual(jasmine.objectContaining({ id: 123 }));
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
