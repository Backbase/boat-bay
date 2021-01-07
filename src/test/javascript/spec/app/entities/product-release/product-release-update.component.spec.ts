import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { ProductReleaseUpdateComponent } from 'app/entities/product-release/product-release-update.component';
import { ProductReleaseService } from 'app/entities/product-release/product-release.service';
import { ProductRelease } from 'app/shared/model/product-release.model';

describe('Component Tests', () => {
  describe('ProductRelease Management Update Component', () => {
    let comp: ProductReleaseUpdateComponent;
    let fixture: ComponentFixture<ProductReleaseUpdateComponent>;
    let service: ProductReleaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ProductReleaseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProductReleaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductReleaseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductReleaseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProductRelease(123);
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
        const entity = new ProductRelease();
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
