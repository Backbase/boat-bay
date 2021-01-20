import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityUpdateComponent } from 'app/entities/capability/capability-update.component';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { Capability } from 'app/shared/model/capability.model';

describe('Component Tests', () => {
  describe('Capability Management Update Component', () => {
    let comp: CapabilityUpdateComponent;
    let fixture: ComponentFixture<CapabilityUpdateComponent>;
    let service: CapabilityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CapabilityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Capability(123);
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
        const entity = new Capability();
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
