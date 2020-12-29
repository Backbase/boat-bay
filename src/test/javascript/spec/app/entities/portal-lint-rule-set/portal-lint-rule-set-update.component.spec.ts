import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { PortalLintRuleSetUpdateComponent } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set-update.component';
import { PortalLintRuleSetService } from 'app/entities/portal-lint-rule-set/portal-lint-rule-set.service';
import { PortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

describe('Component Tests', () => {
  describe('PortalLintRuleSet Management Update Component', () => {
    let comp: PortalLintRuleSetUpdateComponent;
    let fixture: ComponentFixture<PortalLintRuleSetUpdateComponent>;
    let service: PortalLintRuleSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleSetUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PortalLintRuleSetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalLintRuleSetUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleSetService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PortalLintRuleSet(123);
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
        const entity = new PortalLintRuleSet();
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
