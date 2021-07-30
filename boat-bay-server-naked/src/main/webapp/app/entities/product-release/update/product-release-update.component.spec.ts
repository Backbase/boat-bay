jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductReleaseService } from '../service/product-release.service';
import { IProductRelease, ProductRelease } from '../product-release.model';
import { ISpec } from 'app/entities/spec/spec.model';
import { SpecService } from 'app/entities/spec/service/spec.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductReleaseUpdateComponent } from './product-release-update.component';

describe('Component Tests', () => {
  describe('ProductRelease Management Update Component', () => {
    let comp: ProductReleaseUpdateComponent;
    let fixture: ComponentFixture<ProductReleaseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productReleaseService: ProductReleaseService;
    let specService: SpecService;
    let productService: ProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductReleaseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductReleaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductReleaseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productReleaseService = TestBed.inject(ProductReleaseService);
      specService = TestBed.inject(SpecService);
      productService = TestBed.inject(ProductService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Spec query and add missing value', () => {
        const productRelease: IProductRelease = { id: 456 };
        const specs: ISpec[] = [{ id: 64914 }];
        productRelease.specs = specs;

        const specCollection: ISpec[] = [{ id: 7792 }];
        jest.spyOn(specService, 'query').mockReturnValue(of(new HttpResponse({ body: specCollection })));
        const additionalSpecs = [...specs];
        const expectedCollection: ISpec[] = [...additionalSpecs, ...specCollection];
        jest.spyOn(specService, 'addSpecToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        expect(specService.query).toHaveBeenCalled();
        expect(specService.addSpecToCollectionIfMissing).toHaveBeenCalledWith(specCollection, ...additionalSpecs);
        expect(comp.specsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Product query and add missing value', () => {
        const productRelease: IProductRelease = { id: 456 };
        const product: IProduct = { id: 95546 };
        productRelease.product = product;

        const productCollection: IProduct[] = [{ id: 54545 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const productRelease: IProductRelease = { id: 456 };
        const specs: ISpec = { id: 99291 };
        productRelease.specs = [specs];
        const product: IProduct = { id: 68573 };
        productRelease.product = product;

        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(productRelease));
        expect(comp.specsSharedCollection).toContain(specs);
        expect(comp.productsSharedCollection).toContain(product);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductRelease>>();
        const productRelease = { id: 123 };
        jest.spyOn(productReleaseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productRelease }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productReleaseService.update).toHaveBeenCalledWith(productRelease);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductRelease>>();
        const productRelease = new ProductRelease();
        jest.spyOn(productReleaseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: productRelease }));
        saveSubject.complete();

        // THEN
        expect(productReleaseService.create).toHaveBeenCalledWith(productRelease);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ProductRelease>>();
        const productRelease = { id: 123 };
        jest.spyOn(productReleaseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ productRelease });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productReleaseService.update).toHaveBeenCalledWith(productRelease);
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

      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedSpec', () => {
        it('Should return option if no Spec is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedSpec(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Spec for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedSpec(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Spec is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedSpec(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
