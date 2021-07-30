import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LintReportService } from '../service/lint-report.service';

import { LintReportComponent } from './lint-report.component';

describe('Component Tests', () => {
  describe('LintReport Management Component', () => {
    let comp: LintReportComponent;
    let fixture: ComponentFixture<LintReportComponent>;
    let service: LintReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintReportComponent],
      })
        .overrideTemplate(LintReportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintReportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LintReportService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.lintReports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
