jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SpecTypeService } from '../service/spec-type.service';
import { ISpecType, SpecType } from '../spec-type.model';

import { SpecTypeUpdateComponent } from './spec-type-update.component';

describe('Component Tests', () => {
  describe('SpecType Management Update Component', () => {
    let comp: SpecTypeUpdateComponent;
    let fixture: ComponentFixture<SpecTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let specTypeService: SpecTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpecTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SpecTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      specTypeService = TestBed.inject(SpecTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const specType: ISpecType = { id: 456 };

        activatedRoute.data = of({ specType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(specType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpecType>>();
        const specType = { id: 123 };
        jest.spyOn(specTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ specType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: specType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(specTypeService.update).toHaveBeenCalledWith(specType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpecType>>();
        const specType = new SpecType();
        jest.spyOn(specTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ specType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: specType }));
        saveSubject.complete();

        // THEN
        expect(specTypeService.create).toHaveBeenCalledWith(specType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpecType>>();
        const specType = { id: 123 };
        jest.spyOn(specTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ specType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(specTypeService.update).toHaveBeenCalledWith(specType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
