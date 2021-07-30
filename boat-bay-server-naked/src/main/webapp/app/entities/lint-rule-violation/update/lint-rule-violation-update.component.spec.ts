jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LintRuleViolationService } from '../service/lint-rule-violation.service';
import { ILintRuleViolation, LintRuleViolation } from '../lint-rule-violation.model';
import { ILintRule } from 'app/entities/lint-rule/lint-rule.model';
import { LintRuleService } from 'app/entities/lint-rule/service/lint-rule.service';
import { ILintReport } from 'app/entities/lint-report/lint-report.model';
import { LintReportService } from 'app/entities/lint-report/service/lint-report.service';

import { LintRuleViolationUpdateComponent } from './lint-rule-violation-update.component';

describe('Component Tests', () => {
  describe('LintRuleViolation Management Update Component', () => {
    let comp: LintRuleViolationUpdateComponent;
    let fixture: ComponentFixture<LintRuleViolationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lintRuleViolationService: LintRuleViolationService;
    let lintRuleService: LintRuleService;
    let lintReportService: LintReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintRuleViolationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LintRuleViolationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleViolationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lintRuleViolationService = TestBed.inject(LintRuleViolationService);
      lintRuleService = TestBed.inject(LintRuleService);
      lintReportService = TestBed.inject(LintReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LintRule query and add missing value', () => {
        const lintRuleViolation: ILintRuleViolation = { id: 456 };
        const lintRule: ILintRule = { id: 78545 };
        lintRuleViolation.lintRule = lintRule;

        const lintRuleCollection: ILintRule[] = [{ id: 65658 }];
        jest.spyOn(lintRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: lintRuleCollection })));
        const additionalLintRules = [lintRule];
        const expectedCollection: ILintRule[] = [...additionalLintRules, ...lintRuleCollection];
        jest.spyOn(lintRuleService, 'addLintRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        expect(lintRuleService.query).toHaveBeenCalled();
        expect(lintRuleService.addLintRuleToCollectionIfMissing).toHaveBeenCalledWith(lintRuleCollection, ...additionalLintRules);
        expect(comp.lintRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LintReport query and add missing value', () => {
        const lintRuleViolation: ILintRuleViolation = { id: 456 };
        const lintReport: ILintReport = { id: 93136 };
        lintRuleViolation.lintReport = lintReport;

        const lintReportCollection: ILintReport[] = [{ id: 73376 }];
        jest.spyOn(lintReportService, 'query').mockReturnValue(of(new HttpResponse({ body: lintReportCollection })));
        const additionalLintReports = [lintReport];
        const expectedCollection: ILintReport[] = [...additionalLintReports, ...lintReportCollection];
        jest.spyOn(lintReportService, 'addLintReportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        expect(lintReportService.query).toHaveBeenCalled();
        expect(lintReportService.addLintReportToCollectionIfMissing).toHaveBeenCalledWith(lintReportCollection, ...additionalLintReports);
        expect(comp.lintReportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lintRuleViolation: ILintRuleViolation = { id: 456 };
        const lintRule: ILintRule = { id: 36127 };
        lintRuleViolation.lintRule = lintRule;
        const lintReport: ILintReport = { id: 26626 };
        lintRuleViolation.lintReport = lintReport;

        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lintRuleViolation));
        expect(comp.lintRulesSharedCollection).toContain(lintRule);
        expect(comp.lintReportsSharedCollection).toContain(lintReport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRuleViolation>>();
        const lintRuleViolation = { id: 123 };
        jest.spyOn(lintRuleViolationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintRuleViolation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lintRuleViolationService.update).toHaveBeenCalledWith(lintRuleViolation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRuleViolation>>();
        const lintRuleViolation = new LintRuleViolation();
        jest.spyOn(lintRuleViolationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintRuleViolation }));
        saveSubject.complete();

        // THEN
        expect(lintRuleViolationService.create).toHaveBeenCalledWith(lintRuleViolation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRuleViolation>>();
        const lintRuleViolation = { id: 123 };
        jest.spyOn(lintRuleViolationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRuleViolation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lintRuleViolationService.update).toHaveBeenCalledWith(lintRuleViolation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLintRuleById', () => {
        it('Should return tracked LintRule primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLintRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLintReportById', () => {
        it('Should return tracked LintReport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLintReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
