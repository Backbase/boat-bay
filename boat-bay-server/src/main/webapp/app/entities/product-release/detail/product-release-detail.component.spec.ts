import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductReleaseDetailComponent } from './product-release-detail.component';

describe('Component Tests', () => {
  describe('ProductRelease Management Detail Component', () => {
    let comp: ProductReleaseDetailComponent;
    let fixture: ComponentFixture<ProductReleaseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProductReleaseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ productRelease: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProductReleaseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductReleaseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load productRelease on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.productRelease).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
