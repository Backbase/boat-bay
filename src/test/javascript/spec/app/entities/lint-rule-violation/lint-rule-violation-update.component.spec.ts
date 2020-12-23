import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleViolationUpdateComponent } from 'app/entities/lint-rule-violation/lint-rule-violation-update.component';
import { LintRuleViolationService } from 'app/entities/lint-rule-violation/lint-rule-violation.service';
import { LintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

describe('Component Tests', () => {
  describe('LintRuleViolation Management Update Component', () => {
    let comp: LintRuleViolationUpdateComponent;
    let fixture: ComponentFixture<LintRuleViolationUpdateComponent>;
    let service: LintRuleViolationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleViolationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LintRuleViolationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleViolationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleViolationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LintRuleViolation(123);
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
        const entity = new LintRuleViolation();
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
