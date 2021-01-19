import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { LintRuleSetUpdateComponent } from 'app/entities/lint-rule-set/lint-rule-set-update.component';
import { LintRuleSetService } from 'app/entities/lint-rule-set/lint-rule-set.service';
import { LintRuleSet } from 'app/shared/model/lint-rule-set.model';

describe('Component Tests', () => {
  describe('LintRuleSet Management Update Component', () => {
    let comp: LintRuleSetUpdateComponent;
    let fixture: ComponentFixture<LintRuleSetUpdateComponent>;
    let service: LintRuleSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [LintRuleSetUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LintRuleSetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleSetUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LintRuleSetService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LintRuleSet(123);
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
        const entity = new LintRuleSet();
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
