jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SpecService } from '../service/spec.service';
import { ISpec, Spec } from '../spec.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';
import { ICapability } from 'app/entities/capability/capability.model';
import { CapabilityService } from 'app/entities/capability/service/capability.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { ISpecType } from 'app/entities/spec-type/spec-type.model';
import { SpecTypeService } from 'app/entities/spec-type/service/spec-type.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { ServiceDefinitionService } from 'app/entities/service-definition/service/service-definition.service';

import { SpecUpdateComponent } from './spec-update.component';

describe('Component Tests', () => {
  describe('Spec Management Update Component', () => {
    let comp: SpecUpdateComponent;
    let fixture: ComponentFixture<SpecUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let specService: SpecService;
    let portalService: PortalService;
    let capabilityService: CapabilityService;
    let productService: ProductService;
    let sourceService: SourceService;
    let specTypeService: SpecTypeService;
    let tagService: TagService;
    let serviceDefinitionService: ServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpecUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SpecUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      specService = TestBed.inject(SpecService);
      portalService = TestBed.inject(PortalService);
      capabilityService = TestBed.inject(CapabilityService);
      productService = TestBed.inject(ProductService);
      sourceService = TestBed.inject(SourceService);
      specTypeService = TestBed.inject(SpecTypeService);
      tagService = TestBed.inject(TagService);
      serviceDefinitionService = TestBed.inject(ServiceDefinitionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call previousSpec query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const previousSpec: ISpec = { id: 27308 };
        spec.previousSpec = previousSpec;

        const previousSpecCollection: ISpec[] = [{ id: 74709 }];
        jest.spyOn(specService, 'query').mockReturnValue(of(new HttpResponse({ body: previousSpecCollection })));
        const expectedCollection: ISpec[] = [previousSpec, ...previousSpecCollection];
        jest.spyOn(specService, 'addSpecToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(specService.query).toHaveBeenCalled();
        expect(specService.addSpecToCollectionIfMissing).toHaveBeenCalledWith(previousSpecCollection, previousSpec);
        expect(comp.previousSpecsCollection).toEqual(expectedCollection);
      });

      it('Should call Portal query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const portal: IPortal = { id: 32215 };
        spec.portal = portal;

        const portalCollection: IPortal[] = [{ id: 38168 }];
        jest.spyOn(portalService, 'query').mockReturnValue(of(new HttpResponse({ body: portalCollection })));
        const additionalPortals = [portal];
        const expectedCollection: IPortal[] = [...additionalPortals, ...portalCollection];
        jest.spyOn(portalService, 'addPortalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(portalService.query).toHaveBeenCalled();
        expect(portalService.addPortalToCollectionIfMissing).toHaveBeenCalledWith(portalCollection, ...additionalPortals);
        expect(comp.portalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Capability query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const capability: ICapability = { id: 22518 };
        spec.capability = capability;

        const capabilityCollection: ICapability[] = [{ id: 9816 }];
        jest.spyOn(capabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: capabilityCollection })));
        const additionalCapabilities = [capability];
        const expectedCollection: ICapability[] = [...additionalCapabilities, ...capabilityCollection];
        jest.spyOn(capabilityService, 'addCapabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(capabilityService.query).toHaveBeenCalled();
        expect(capabilityService.addCapabilityToCollectionIfMissing).toHaveBeenCalledWith(capabilityCollection, ...additionalCapabilities);
        expect(comp.capabilitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Product query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const product: IProduct = { id: 20056 };
        spec.product = product;

        const productCollection: IProduct[] = [{ id: 48664 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Source query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const source: ISource = { id: 64878 };
        spec.source = source;

        const sourceCollection: ISource[] = [{ id: 45236 }];
        jest.spyOn(sourceService, 'query').mockReturnValue(of(new HttpResponse({ body: sourceCollection })));
        const additionalSources = [source];
        const expectedCollection: ISource[] = [...additionalSources, ...sourceCollection];
        jest.spyOn(sourceService, 'addSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(sourceService.query).toHaveBeenCalled();
        expect(sourceService.addSourceToCollectionIfMissing).toHaveBeenCalledWith(sourceCollection, ...additionalSources);
        expect(comp.sourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SpecType query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const specType: ISpecType = { id: 23162 };
        spec.specType = specType;

        const specTypeCollection: ISpecType[] = [{ id: 43866 }];
        jest.spyOn(specTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: specTypeCollection })));
        const additionalSpecTypes = [specType];
        const expectedCollection: ISpecType[] = [...additionalSpecTypes, ...specTypeCollection];
        jest.spyOn(specTypeService, 'addSpecTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(specTypeService.query).toHaveBeenCalled();
        expect(specTypeService.addSpecTypeToCollectionIfMissing).toHaveBeenCalledWith(specTypeCollection, ...additionalSpecTypes);
        expect(comp.specTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tag query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const tags: ITag[] = [{ id: 94851 }];
        spec.tags = tags;

        const tagCollection: ITag[] = [{ id: 63554 }];
        jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
        const additionalTags = [...tags];
        const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
        jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(tagService.query).toHaveBeenCalled();
        expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags);
        expect(comp.tagsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ServiceDefinition query and add missing value', () => {
        const spec: ISpec = { id: 456 };
        const serviceDefinition: IServiceDefinition = { id: 98853 };
        spec.serviceDefinition = serviceDefinition;

        const serviceDefinitionCollection: IServiceDefinition[] = [{ id: 8871 }];
        jest.spyOn(serviceDefinitionService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceDefinitionCollection })));
        const additionalServiceDefinitions = [serviceDefinition];
        const expectedCollection: IServiceDefinition[] = [...additionalServiceDefinitions, ...serviceDefinitionCollection];
        jest.spyOn(serviceDefinitionService, 'addServiceDefinitionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(serviceDefinitionService.query).toHaveBeenCalled();
        expect(serviceDefinitionService.addServiceDefinitionToCollectionIfMissing).toHaveBeenCalledWith(
          serviceDefinitionCollection,
          ...additionalServiceDefinitions
        );
        expect(comp.serviceDefinitionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const spec: ISpec = { id: 456 };
        const previousSpec: ISpec = { id: 18307 };
        spec.previousSpec = previousSpec;
        const portal: IPortal = { id: 72136 };
        spec.portal = portal;
        const capability: ICapability = { id: 52516 };
        spec.capability = capability;
        const product: IProduct = { id: 33718 };
        spec.product = product;
        const source: ISource = { id: 54361 };
        spec.source = source;
        const specType: ISpecType = { id: 19423 };
        spec.specType = specType;
        const tags: ITag = { id: 37967 };
        spec.tags = [tags];
        const serviceDefinition: IServiceDefinition = { id: 42350 };
        spec.serviceDefinition = serviceDefinition;

        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(spec));
        expect(comp.previousSpecsCollection).toContain(previousSpec);
        expect(comp.portalsSharedCollection).toContain(portal);
        expect(comp.capabilitiesSharedCollection).toContain(capability);
        expect(comp.productsSharedCollection).toContain(product);
        expect(comp.sourcesSharedCollection).toContain(source);
        expect(comp.specTypesSharedCollection).toContain(specType);
        expect(comp.tagsSharedCollection).toContain(tags);
        expect(comp.serviceDefinitionsSharedCollection).toContain(serviceDefinition);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Spec>>();
        const spec = { id: 123 };
        jest.spyOn(specService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: spec }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(specService.update).toHaveBeenCalledWith(spec);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Spec>>();
        const spec = new Spec();
        jest.spyOn(specService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: spec }));
        saveSubject.complete();

        // THEN
        expect(specService.create).toHaveBeenCalledWith(spec);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Spec>>();
        const spec = { id: 123 };
        jest.spyOn(specService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spec });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(specService.update).toHaveBeenCalledWith(spec);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSpecById', () => {
        it('Should return tracked Spec primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpecById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPortalById', () => {
        it('Should return tracked Portal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPortalById(0, entity);
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

      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSourceById', () => {
        it('Should return tracked Source primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSourceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSpecTypeById', () => {
        it('Should return tracked SpecType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpecTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTagById', () => {
        it('Should return tracked Tag primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTagById(0, entity);
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

    describe('Getting selected relationships', () => {
      describe('getSelectedTag', () => {
        it('Should return option if no Tag is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedTag(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Tag for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedTag(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Tag is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedTag(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
