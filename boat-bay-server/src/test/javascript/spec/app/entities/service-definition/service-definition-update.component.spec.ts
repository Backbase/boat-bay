import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { ServiceDefinitionUpdateComponent } from 'app/entities/service-definition/service-definition-update.component';
import { ServiceDefinitionService } from 'app/entities/service-definition/service-definition.service';
import { ServiceDefinition } from 'app/shared/model/service-definition.model';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Update Component', () => {
    let comp: ServiceDefinitionUpdateComponent;
    let fixture: ComponentFixture<ServiceDefinitionUpdateComponent>;
    let service: ServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ServiceDefinitionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ServiceDefinitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceDefinitionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServiceDefinitionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ServiceDefinition(123);
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
        const entity = new ServiceDefinition();
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
