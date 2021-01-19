import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceUpdateComponent } from 'app/entities/capability-service/capability-service-update.component';
import { CapabilityServiceService } from 'app/entities/capability-service/capability-service.service';
import { CapabilityService } from 'app/shared/model/capability-service.model';

describe('Component Tests', () => {
  describe('CapabilityService Management Update Component', () => {
    let comp: CapabilityServiceUpdateComponent;
    let fixture: ComponentFixture<CapabilityServiceUpdateComponent>;
    let service: CapabilityServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CapabilityServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityServiceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityServiceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CapabilityService(123);
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
        const entity = new CapabilityService();
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
