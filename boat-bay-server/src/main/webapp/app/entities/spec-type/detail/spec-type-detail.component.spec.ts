import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpecTypeDetailComponent } from './spec-type-detail.component';

describe('Component Tests', () => {
  describe('SpecType Management Detail Component', () => {
    let comp: SpecTypeDetailComponent;
    let fixture: ComponentFixture<SpecTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SpecTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ specType: { id: 123 } }) },
          },
        ],
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
        expect(comp.specType).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
