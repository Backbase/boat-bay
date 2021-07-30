import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LintReportDetailComponent } from './lint-report-detail.component';

describe('Component Tests', () => {
  describe('LintReport Management Detail Component', () => {
    let comp: LintReportDetailComponent;
    let fixture: ComponentFixture<LintReportDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LintReportDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ lintReport: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LintReportDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintReportDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lintReport on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lintReport).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
