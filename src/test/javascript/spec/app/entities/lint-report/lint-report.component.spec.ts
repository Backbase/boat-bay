import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { LintReportComponent } from 'app/entities/lint-report/lint-report.component';
import { LintReportService } from 'app/entities/lint-report/lint-report.service';
import { LintReport } from 'app/shared/model/lint-report.model';

describe('Component Tests', () => {
  describe('LintReport Management Component', () => {
    let comp: LintReportComponent;
    let fixture: ComponentFixture<LintReportComponent>;
    let service: LintReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintReportComponent],
      })
        .overrideTemplate(LintReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintReportComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintReportService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LintReport(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lintReports && comp.lintReports[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
