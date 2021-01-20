import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SourceSpecUpdateComponent } from 'app/entities/source-spec/source-spec-update.component';
import { SourceSpecService } from 'app/entities/source-spec/source-spec.service';
import { SourceSpec } from 'app/shared/model/source-spec.model';

describe('Component Tests', () => {
  describe('SourceSpec Management Update Component', () => {
    let comp: SourceSpecUpdateComponent;
    let fixture: ComponentFixture<SourceSpecUpdateComponent>;
    let service: SourceSpecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourceSpecUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SourceSpecUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourceSpecUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SourceSpecService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SourceSpec(123);
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
        const entity = new SourceSpec();
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
