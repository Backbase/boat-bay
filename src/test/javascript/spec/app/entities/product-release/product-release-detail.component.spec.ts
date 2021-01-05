import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { ProductReleaseDetailComponent } from 'app/entities/product-release/product-release-detail.component';
import { ProductRelease } from 'app/shared/model/product-release.model';

describe('Component Tests', () => {
  describe('ProductRelease Management Detail Component', () => {
    let comp: ProductReleaseDetailComponent;
    let fixture: ComponentFixture<ProductReleaseDetailComponent>;
    const route = ({ data: of({ productRelease: new ProductRelease(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ProductReleaseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.productRelease).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
