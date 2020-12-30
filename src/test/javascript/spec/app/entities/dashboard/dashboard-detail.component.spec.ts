import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { DashboardDetailComponent } from 'app/entities/dashboard/dashboard-detail.component';
import { Dashboard } from 'app/shared/model/dashboard.model';

describe('Component Tests', () => {
  describe('Dashboard Management Detail Component', () => {
    let comp: DashboardDetailComponent;
    let fixture: ComponentFixture<DashboardDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ dashboard: new Dashboard(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [DashboardDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DashboardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DashboardDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load dashboard on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dashboard).toEqual(jasmine.objectContaining({ id: 123 }));
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
