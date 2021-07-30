jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ZallyConfigService } from '../service/zally-config.service';
import { IZallyConfig, ZallyConfig } from '../zally-config.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';

import { ZallyConfigUpdateComponent } from './zally-config-update.component';

describe('Component Tests', () => {
  describe('ZallyConfig Management Update Component', () => {
    let comp: ZallyConfigUpdateComponent;
    let fixture: ComponentFixture<ZallyConfigUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let zallyConfigService: ZallyConfigService;
    let portalService: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ZallyConfigUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ZallyConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ZallyConfigUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      zallyConfigService = TestBed.inject(ZallyConfigService);
      portalService = TestBed.inject(PortalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call portal query and add missing value', () => {
        const zallyConfig: IZallyConfig = { id: 456 };
        const portal: IPortal = { id: 99923 };
        zallyConfig.portal = portal;

        const portalCollection: IPortal[] = [{ id: 17286 }];
        jest.spyOn(portalService, 'query').mockReturnValue(of(new HttpResponse({ body: portalCollection })));
        const expectedCollection: IPortal[] = [portal, ...portalCollection];
        jest.spyOn(portalService, 'addPortalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ zallyConfig });
        comp.ngOnInit();

        expect(portalService.query).toHaveBeenCalled();
        expect(portalService.addPortalToCollectionIfMissing).toHaveBeenCalledWith(portalCollection, portal);
        expect(comp.portalsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const zallyConfig: IZallyConfig = { id: 456 };
        const portal: IPortal = { id: 88053 };
        zallyConfig.portal = portal;

        activatedRoute.data = of({ zallyConfig });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(zallyConfig));
        expect(comp.portalsCollection).toContain(portal);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ZallyConfig>>();
        const zallyConfig = { id: 123 };
        jest.spyOn(zallyConfigService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ zallyConfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: zallyConfig }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(zallyConfigService.update).toHaveBeenCalledWith(zallyConfig);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ZallyConfig>>();
        const zallyConfig = new ZallyConfig();
        jest.spyOn(zallyConfigService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ zallyConfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: zallyConfig }));
        saveSubject.complete();

        // THEN
        expect(zallyConfigService.create).toHaveBeenCalledWith(zallyConfig);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ZallyConfig>>();
        const zallyConfig = { id: 123 };
        jest.spyOn(zallyConfigService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ zallyConfig });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(zallyConfigService.update).toHaveBeenCalledWith(zallyConfig);
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
