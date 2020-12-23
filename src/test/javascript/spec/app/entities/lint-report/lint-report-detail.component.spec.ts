import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintReportDetailComponent } from 'app/entities/lint-report/lint-report-detail.component';
import { LintReport } from 'app/shared/model/lint-report.model';

describe('Component Tests', () => {
  describe('LintReport Management Detail Component', () => {
    let comp: LintReportDetailComponent;
    let fixture: ComponentFixture<LintReportDetailComponent>;
    const route = ({ data: of({ lintReport: new LintReport(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintReportDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.lintReport).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
