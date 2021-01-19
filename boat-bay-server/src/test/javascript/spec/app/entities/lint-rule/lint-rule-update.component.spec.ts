import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleUpdateComponent } from 'app/entities/lint-rule/lint-rule-update.component';
import { LintRuleService } from 'app/entities/lint-rule/lint-rule.service';
import { LintRule } from 'app/shared/model/lint-rule.model';

describe('Component Tests', () => {
  describe('LintRule Management Update Component', () => {
    let comp: LintRuleUpdateComponent;
    let fixture: ComponentFixture<LintRuleUpdateComponent>;
    let service: LintRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LintRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LintRule(123);
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
        const entity = new LintRule();
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
