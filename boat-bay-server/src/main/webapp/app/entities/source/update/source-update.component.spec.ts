jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SourceService } from '../service/source.service';
import { ISource, Source } from '../source.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service/service-definition.service';

import { SourceUpdateComponent } from './source-update.component';

describe('Component Tests', () => {
  describe('Source Management Update Component', () => {
    let comp: SourceUpdateComponent;
    let fixture: ComponentFixture<SourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sourceService: SourceService;
    let portalService: PortalService;
    let productService: ProductService;
    let capabilityService: CapabilityService;
    let serviceDefinitionService: ServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sourceService = TestBed.inject(SourceService);
      portalService = TestBed.inject(PortalService);
      productService = TestBed.inject(ProductService);
      capabilityService = TestBed.inject(CapabilityService);
      serviceDefinitionService = TestBed.inject(ServiceDefinitionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Portal query and add missing value', () => {
        const source: ISource = { id: 456 };
        const portal: IPortal = { id: 92382 };
        source.portal = portal;

        const portalCollection: IPortal[] = [{ id: 71410 }];
        jest.spyOn(portalService, 'query').mockReturnValue(of(new HttpResponse({ body: portalCollection })));
        const additionalPortals = [portal];
        const expectedCollection: IPortal[] = [...additionalPortals, ...portalCollection];
        jest.spyOn(portalService, 'addPortalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ source });
        comp.ngOnInit();

        expect(portalService.query).toHaveBeenCalled();
        expect(portalService.addPortalToCollectionIfMissing).toHaveBeenCalledWith(portalCollection, ...additionalPortals);
        expect(comp.portalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Product query and add missing value', () => {
        const source: ISource = { id: 456 };
        const product: IProduct = { id: 9735 };
        source.product = product;

        const productCollection: IProduct[] = [{ id: 62112 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ source });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Capability query and add missing value', () => {
        const source: ISource = { id: 456 };
        const capability: ICapability = { id: 89156 };
        source.capability = capability;

        const capabilityCollection: ICapability[] = [{ id: 19129 }];
        jest.spyOn(capabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: capabilityCollection })));
        const additionalCapabilities = [capability];
        const expectedCollection: ICapability[] = [...additionalCapabilities, ...capabilityCollection];
        jest.spyOn(capabilityService, 'addCapabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ source });
        comp.ngOnInit();

        expect(capabilityService.query).toHaveBeenCalled();
        expect(capabilityService.addCapabilityToCollectionIfMissing).toHaveBeenCalledWith(capabilityCollection, ...additionalCapabilities);
        expect(comp.capabilitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ServiceDefinition query and add missing value', () => {
        const source: ISource = { id: 456 };
        const serviceDefinition: IServiceDefinition = { id: 66976 };
        source.serviceDefinition = serviceDefinition;

        const serviceDefinitionCollection: IServiceDefinition[] = [{ id: 92224 }];
        jest.spyOn(serviceDefinitionService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceDefinitionCollection })));
        const additionalServiceDefinitions = [serviceDefinition];
        const expectedCollection: IServiceDefinition[] = [...additionalServiceDefinitions, ...serviceDefinitionCollection];
        jest.spyOn(serviceDefinitionService, 'addServiceDefinitionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ source });
        comp.ngOnInit();

        expect(serviceDefinitionService.query).toHaveBeenCalled();
        expect(serviceDefinitionService.addServiceDefinitionToCollectionIfMissing).toHaveBeenCalledWith(
          serviceDefinitionCollection,
          ...additionalServiceDefinitions
        );
        expect(comp.serviceDefinitionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const source: ISource = { id: 456 };
        const portal: IPortal = { id: 37094 };
        source.portal = portal;
        const product: IProduct = { id: 52128 };
        source.product = product;
        const capability: ICapability = { id: 23139 };
        source.capability = capability;
        const serviceDefinition: IServiceDefinition = { id: 77254 };
        source.serviceDefinition = serviceDefinition;

        activatedRoute.data = of({ source });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(source));
        expect(comp.portalsSharedCollection).toContain(portal);
        expect(comp.productsSharedCollection).toContain(product);
        expect(comp.capabilitiesSharedCollection).toContain(capability);
        expect(comp.serviceDefinitionsSharedCollection).toContain(serviceDefinition);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Source>>();
        const source = { id: 123 };
        jest.spyOn(sourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ source });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: source }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sourceService.update).toHaveBeenCalledWith(source);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Source>>();
        const source = new Source();
        jest.spyOn(sourceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ source });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: source }));
        saveSubject.complete();

        // THEN
        expect(sourceService.create).toHaveBeenCalledWith(source);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Source>>();
        const source = { id: 123 };
        jest.spyOn(sourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ source });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sourceService.update).toHaveBeenCalledWith(source);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPortalById', () => {
        it('Should return tracked Portal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPortalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCapabilityById', () => {
        it('Should return tracked Capability primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCapabilityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackServiceDefinitionById', () => {
        it('Should return tracked ServiceDefinition primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackServiceDefinitionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
