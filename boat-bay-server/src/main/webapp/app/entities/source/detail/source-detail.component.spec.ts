import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SourceDetailComponent } from './source-detail.component';

describe('Component Tests', () => {
  describe('Source Management Detail Component', () => {
    let comp: SourceDetailComponent;
    let fixture: ComponentFixture<SourceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SourceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ source: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SourceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SourceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load source on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.source).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
