jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LintReportService } from '../service/lint-report.service';
import { ILintReport, LintReport } from '../lint-report.model';
import { ISpec } from 'app/entities/spec/spec.model';
import { SpecService } from 'app/entities/spec/service/spec.service';

import { LintReportUpdateComponent } from './lint-report-update.component';

describe('Component Tests', () => {
  describe('LintReport Management Update Component', () => {
    let comp: LintReportUpdateComponent;
    let fixture: ComponentFixture<LintReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lintReportService: LintReportService;
    let specService: SpecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LintReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lintReportService = TestBed.inject(LintReportService);
      specService = TestBed.inject(SpecService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call spec query and add missing value', () => {
        const lintReport: ILintReport = { id: 456 };
        const spec: ISpec = { id: 9026 };
        lintReport.spec = spec;

        const specCollection: ISpec[] = [{ id: 75922 }];
        jest.spyOn(specService, 'query').mockReturnValue(of(new HttpResponse({ body: specCollection })));
        const expectedCollection: ISpec[] = [spec, ...specCollection];
        jest.spyOn(specService, 'addSpecToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lintReport });
        comp.ngOnInit();

        expect(specService.query).toHaveBeenCalled();
        expect(specService.addSpecToCollectionIfMissing).toHaveBeenCalledWith(specCollection, spec);
        expect(comp.specsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lintReport: ILintReport = { id: 456 };
        const spec: ISpec = { id: 1307 };
        lintReport.spec = spec;

        activatedRoute.data = of({ lintReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lintReport));
        expect(comp.specsCollection).toContain(spec);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintReport>>();
        const lintReport = { id: 123 };
        jest.spyOn(lintReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lintReportService.update).toHaveBeenCalledWith(lintReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintReport>>();
        const lintReport = new LintReport();
        jest.spyOn(lintReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintReport }));
        saveSubject.complete();

        // THEN
        expect(lintReportService.create).toHaveBeenCalledWith(lintReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintReport>>();
        const lintReport = { id: 123 };
        jest.spyOn(lintReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lintReportService.update).toHaveBeenCalledWith(lintReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSpecById', () => {
        it('Should return tracked Spec primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpecById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
