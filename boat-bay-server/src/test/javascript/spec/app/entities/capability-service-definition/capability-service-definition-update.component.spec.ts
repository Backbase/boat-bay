import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceDefinitionUpdateComponent } from 'app/entities/capability-service-definition/capability-service-definition-update.component';
import { CapabilityServiceDefinitionService } from 'app/entities/capability-service-definition/capability-service-definition.service';
import { CapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';

describe('Component Tests', () => {
  describe('CapabilityServiceDefinition Management Update Component', () => {
    let comp: CapabilityServiceDefinitionUpdateComponent;
    let fixture: ComponentFixture<CapabilityServiceDefinitionUpdateComponent>;
    let service: CapabilityServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceDefinitionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CapabilityServiceDefinitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityServiceDefinitionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityServiceDefinitionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CapabilityServiceDefinition(123);
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
        const entity = new CapabilityServiceDefinition();
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
