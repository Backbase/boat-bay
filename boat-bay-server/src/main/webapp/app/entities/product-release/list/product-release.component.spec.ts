import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductReleaseService } from '../service/product-release.service';

import { ProductReleaseComponent } from './product-release.component';

describe('Component Tests', () => {
  describe('ProductRelease Management Component', () => {
    let comp: ProductReleaseComponent;
    let fixture: ComponentFixture<ProductReleaseComponent>;
    let service: ProductReleaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductReleaseComponent],
      })
        .overrideTemplate(ProductReleaseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductReleaseComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProductReleaseService);

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
      expect(comp.productReleases?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
