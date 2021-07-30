import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SourcePathDetailComponent } from './source-path-detail.component';

describe('Component Tests', () => {
  describe('SourcePath Management Detail Component', () => {
    let comp: SourcePathDetailComponent;
    let fixture: ComponentFixture<SourcePathDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SourcePathDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sourcePath: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SourcePathDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SourcePathDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sourcePath on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sourcePath).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
