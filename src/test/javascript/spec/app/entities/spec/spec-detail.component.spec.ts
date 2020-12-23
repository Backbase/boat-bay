import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SpecDetailComponent } from 'app/entities/spec/spec-detail.component';
import { Spec } from 'app/shared/model/spec.model';

describe('Component Tests', () => {
  describe('Spec Management Detail Component', () => {
    let comp: SpecDetailComponent;
    let fixture: ComponentFixture<SpecDetailComponent>;
    const route = ({ data: of({ spec: new Spec(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SpecDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpecDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load spec on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.spec).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
