jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PortalService } from '../service/portal.service';
import { IPortal, Portal } from '../portal.model';

import { PortalUpdateComponent } from './portal-update.component';

describe('Component Tests', () => {
  describe('Portal Management Update Component', () => {
    let comp: PortalUpdateComponent;
    let fixture: ComponentFixture<PortalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let portalService: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PortalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PortalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      portalService = TestBed.inject(PortalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const portal: IPortal = { id: 456 };

        activatedRoute.data = of({ portal });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(portal));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Portal>>();
        const portal = { id: 123 };
        jest.spyOn(portalService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ portal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: portal }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(portalService.update).toHaveBeenCalledWith(portal);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Portal>>();
        const portal = new Portal();
        jest.spyOn(portalService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ portal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: portal }));
        saveSubject.complete();

        // THEN
        expect(portalService.create).toHaveBeenCalledWith(portal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Portal>>();
        const portal = { id: 123 };
        jest.spyOn(portalService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ portal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(portalService.update).toHaveBeenCalledWith(portal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
