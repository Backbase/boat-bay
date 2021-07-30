jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LintRuleService } from '../service/lint-rule.service';
import { ILintRule, LintRule } from '../lint-rule.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { PortalService } from 'app/entities/portal/service/portal.service';

import { LintRuleUpdateComponent } from './lint-rule-update.component';

describe('Component Tests', () => {
  describe('LintRule Management Update Component', () => {
    let comp: LintRuleUpdateComponent;
    let fixture: ComponentFixture<LintRuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lintRuleService: LintRuleService;
    let portalService: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintRuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LintRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lintRuleService = TestBed.inject(LintRuleService);
      portalService = TestBed.inject(PortalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Portal query and add missing value', () => {
        const lintRule: ILintRule = { id: 456 };
        const portal: IPortal = { id: 12289 };
        lintRule.portal = portal;

        const portalCollection: IPortal[] = [{ id: 63298 }];
        jest.spyOn(portalService, 'query').mockReturnValue(of(new HttpResponse({ body: portalCollection })));
        const additionalPortals = [portal];
        const expectedCollection: IPortal[] = [...additionalPortals, ...portalCollection];
        jest.spyOn(portalService, 'addPortalToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lintRule });
        comp.ngOnInit();

        expect(portalService.query).toHaveBeenCalled();
        expect(portalService.addPortalToCollectionIfMissing).toHaveBeenCalledWith(portalCollection, ...additionalPortals);
        expect(comp.portalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lintRule: ILintRule = { id: 456 };
        const portal: IPortal = { id: 98437 };
        lintRule.portal = portal;

        activatedRoute.data = of({ lintRule });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lintRule));
        expect(comp.portalsSharedCollection).toContain(portal);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRule>>();
        const lintRule = { id: 123 };
        jest.spyOn(lintRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintRule }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lintRuleService.update).toHaveBeenCalledWith(lintRule);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRule>>();
        const lintRule = new LintRule();
        jest.spyOn(lintRuleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lintRule }));
        saveSubject.complete();

        // THEN
        expect(lintRuleService.create).toHaveBeenCalledWith(lintRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LintRule>>();
        const lintRule = { id: 123 };
        jest.spyOn(lintRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lintRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lintRuleService.update).toHaveBeenCalledWith(lintRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPortalById', () => {
        it('Should return tracked Portal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPortalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
