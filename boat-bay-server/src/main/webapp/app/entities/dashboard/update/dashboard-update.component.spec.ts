jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DashboardService } from '../service/dashboard.service';
import { IDashboard, Dashboard } from '../dashboard.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';

import { DashboardUpdateComponent } from './dashboard-update.component';

describe('Component Tests', () => {
  describe('Dashboard Management Update Component', () => {
    let comp: DashboardUpdateComponent;
    let fixture: ComponentFixture<DashboardUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dashboardService: DashboardService;
    let portalService: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DashboardUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DashboardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DashboardUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dashboardService = TestBed.inject(DashboardService);
      portalService = TestBed.inject(PortalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call defaultPortal query and add missing value', () => {
        const dashboard: IDashboard = { id: 456 };
        const defaultPortal: IPortal = { id: 44007 };
        dashboard.defaultPortal = defaultPortal;

        const defaultPortalCollection: IPortal[] = [{ id: 53965 }];
        jest.spyOn(portalService, 'query').mockReturnValue(of(new HttpResponse({ body: defaultPortalCollection })));
        const expectedCollection: IPortal[] = [defaultPortal, ...defaultPortalCollection];
        jest.spyOn(portalService, 'addPortalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dashboard });
        comp.ngOnInit();

        expect(portalService.query).toHaveBeenCalled();
        expect(portalService.addPortalToCollectionIfMissing).toHaveBeenCalledWith(defaultPortalCollection, defaultPortal);
        expect(comp.defaultPortalsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dashboard: IDashboard = { id: 456 };
        const defaultPortal: IPortal = { id: 37652 };
        dashboard.defaultPortal = defaultPortal;

        activatedRoute.data = of({ dashboard });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dashboard));
        expect(comp.defaultPortalsCollection).toContain(defaultPortal);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dashboard>>();
        const dashboard = { id: 123 };
        jest.spyOn(dashboardService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dashboard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dashboard }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dashboardService.update).toHaveBeenCalledWith(dashboard);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dashboard>>();
        const dashboard = new Dashboard();
        jest.spyOn(dashboardService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dashboard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dashboard }));
        saveSubject.complete();

        // THEN
        expect(dashboardService.create).toHaveBeenCalledWith(dashboard);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Dashboard>>();
        const dashboard = { id: 123 };
        jest.spyOn(dashboardService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dashboard });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dashboardService.update).toHaveBeenCalledWith(dashboard);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPortalById', () => {
        it('Should return tracked Portal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPortalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
