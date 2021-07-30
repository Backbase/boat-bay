jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ServiceDefinitionService } from '../service/service-definition.service';
import { IServiceDefinition, ServiceDefinition } from '../service-definition.model';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';

import { ServiceDefinitionUpdateComponent } from './service-definition-update.component';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Update Component', () => {
    let comp: ServiceDefinitionUpdateComponent;
    let fixture: ComponentFixture<ServiceDefinitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let serviceDefinitionService: ServiceDefinitionService;
    let capabilityService: CapabilityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServiceDefinitionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ServiceDefinitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceDefinitionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      serviceDefinitionService = TestBed.inject(ServiceDefinitionService);
      capabilityService = TestBed.inject(CapabilityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Capability query and add missing value', () => {
        const serviceDefinition: IServiceDefinition = { id: 456 };
        const capability: ICapability = { id: 71746 };
        serviceDefinition.capability = capability;

        const capabilityCollection: ICapability[] = [{ id: 34573 }];
        jest.spyOn(capabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: capabilityCollection })));
        const additionalCapabilities = [capability];
        const expectedCollection: ICapability[] = [...additionalCapabilities, ...capabilityCollection];
        jest.spyOn(capabilityService, 'addCapabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ serviceDefinition });
        comp.ngOnInit();

        expect(capabilityService.query).toHaveBeenCalled();
        expect(capabilityService.addCapabilityToCollectionIfMissing).toHaveBeenCalledWith(capabilityCollection, ...additionalCapabilities);
        expect(comp.capabilitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const serviceDefinition: IServiceDefinition = { id: 456 };
        const capability: ICapability = { id: 48595 };
        serviceDefinition.capability = capability;

        activatedRoute.data = of({ serviceDefinition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(serviceDefinition));
        expect(comp.capabilitiesSharedCollection).toContain(capability);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ServiceDefinition>>();
        const serviceDefinition = { id: 123 };
        jest.spyOn(serviceDefinitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceDefinition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(serviceDefinitionService.update).toHaveBeenCalledWith(serviceDefinition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ServiceDefinition>>();
        const serviceDefinition = new ServiceDefinition();
        jest.spyOn(serviceDefinitionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceDefinition }));
        saveSubject.complete();

        // THEN
        expect(serviceDefinitionService.create).toHaveBeenCalledWith(serviceDefinition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ServiceDefinition>>();
        const serviceDefinition = { id: 123 };
        jest.spyOn(serviceDefinitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceDefinition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(serviceDefinitionService.update).toHaveBeenCalledWith(serviceDefinition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCapabilityById', () => {
        it('Should return tracked Capability primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCapabilityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
