jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CapabilityService } from '../service/capability.service';
import { ICapability, Capability } from '../capability.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { CapabilityUpdateComponent } from './capability-update.component';

describe('Component Tests', () => {
  describe('Capability Management Update Component', () => {
    let comp: CapabilityUpdateComponent;
    let fixture: ComponentFixture<CapabilityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let capabilityService: CapabilityService;
    let productService: ProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CapabilityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CapabilityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      capabilityService = TestBed.inject(CapabilityService);
      productService = TestBed.inject(ProductService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Product query and add missing value', () => {
        const capability: ICapability = { id: 456 };
        const product: IProduct = { id: 89993 };
        capability.product = product;

        const productCollection: IProduct[] = [{ id: 44563 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ capability });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const capability: ICapability = { id: 456 };
        const product: IProduct = { id: 30467 };
        capability.product = product;

        activatedRoute.data = of({ capability });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(capability));
        expect(comp.productsSharedCollection).toContain(product);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Capability>>();
        const capability = { id: 123 };
        jest.spyOn(capabilityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ capability });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: capability }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(capabilityService.update).toHaveBeenCalledWith(capability);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Capability>>();
        const capability = new Capability();
        jest.spyOn(capabilityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ capability });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: capability }));
        saveSubject.complete();

        // THEN
        expect(capabilityService.create).toHaveBeenCalledWith(capability);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Capability>>();
        const capability = { id: 123 };
        jest.spyOn(capabilityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ capability });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(capabilityService.update).toHaveBeenCalledWith(capability);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
