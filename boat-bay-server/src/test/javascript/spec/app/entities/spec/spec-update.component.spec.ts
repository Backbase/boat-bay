import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SpecUpdateComponent } from 'app/entities/spec/spec-update.component';
import { SpecService } from 'app/entities/spec/spec.service';
import { Spec } from 'app/shared/model/spec.model';

describe('Component Tests', () => {
  describe('Spec Management Update Component', () => {
    let comp: SpecUpdateComponent;
    let fixture: ComponentFixture<SpecUpdateComponent>;
    let service: SpecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SpecUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Spec(123);
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
        const entity = new Spec();
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
