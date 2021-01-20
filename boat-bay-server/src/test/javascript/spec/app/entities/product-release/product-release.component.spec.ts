import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { ProductReleaseComponent } from 'app/entities/product-release/product-release.component';
import { ProductReleaseService } from 'app/entities/product-release/product-release.service';
import { ProductRelease } from 'app/shared/model/product-release.model';

describe('Component Tests', () => {
  describe('ProductRelease Management Component', () => {
    let comp: ProductReleaseComponent;
    let fixture: ComponentFixture<ProductReleaseComponent>;
    let service: ProductReleaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ProductReleaseComponent],
      })
        .overrideTemplate(ProductReleaseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductReleaseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductReleaseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ProductRelease(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.productReleases && comp.productReleases[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
