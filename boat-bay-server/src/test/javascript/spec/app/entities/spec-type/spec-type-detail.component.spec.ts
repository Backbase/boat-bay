import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SpecTypeDetailComponent } from 'app/entities/spec-type/spec-type-detail.component';
import { SpecType } from 'app/shared/model/spec-type.model';

describe('Component Tests', () => {
  describe('SpecType Management Detail Component', () => {
    let comp: SpecTypeDetailComponent;
    let fixture: ComponentFixture<SpecTypeDetailComponent>;
    const route = ({ data: of({ specType: new SpecType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SpecTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpecTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load specType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.specType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
