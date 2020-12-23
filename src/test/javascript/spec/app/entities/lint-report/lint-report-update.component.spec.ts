import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintReportUpdateComponent } from 'app/entities/lint-report/lint-report-update.component';
import { LintReportService } from 'app/entities/lint-report/lint-report.service';
import { LintReport } from 'app/shared/model/lint-report.model';

describe('Component Tests', () => {
  describe('LintReport Management Update Component', () => {
    let comp: LintReportUpdateComponent;
    let fixture: ComponentFixture<LintReportUpdateComponent>;
    let service: LintReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintReportUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LintReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintReportUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintReportService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LintReport(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LintReport();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
