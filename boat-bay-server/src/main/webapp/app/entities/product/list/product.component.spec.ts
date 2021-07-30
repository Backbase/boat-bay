import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductService } from '../service/product.service';

import { ProductComponent } from './product.component';

describe('Component Tests', () => {
  describe('Product Management Component', () => {
    let comp: ProductComponent;
    let fixture: ComponentFixture<ProductComponent>;
    let service: ProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductComponent],
      })
        .overrideTemplate(ProductComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProductService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.products?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
