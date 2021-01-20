import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SpecTypeUpdateComponent } from 'app/entities/spec-type/spec-type-update.component';
import { SpecTypeService } from 'app/entities/spec-type/spec-type.service';
import { SpecType } from 'app/shared/model/spec-type.model';

describe('Component Tests', () => {
  describe('SpecType Management Update Component', () => {
    let comp: SpecTypeUpdateComponent;
    let fixture: ComponentFixture<SpecTypeUpdateComponent>;
    let service: SpecTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SpecTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SpecType(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SpecType();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
