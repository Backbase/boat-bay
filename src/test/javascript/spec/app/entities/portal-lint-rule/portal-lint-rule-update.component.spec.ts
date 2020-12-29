import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleUpdateComponent } from 'app/entities/portal-lint-rule/portal-lint-rule-update.component';
import { PortalLintRuleService } from 'app/entities/portal-lint-rule/portal-lint-rule.service';
import { PortalLintRule } from 'app/shared/model/portal-lint-rule.model';

describe('Component Tests', () => {
  describe('PortalLintRule Management Update Component', () => {
    let comp: PortalLintRuleUpdateComponent;
    let fixture: ComponentFixture<PortalLintRuleUpdateComponent>;
    let service: PortalLintRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PortalLintRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalLintRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PortalLintRule(123);
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
        const entity = new PortalLintRule();
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
