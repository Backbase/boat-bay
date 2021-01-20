import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { PortalLintRuleConfigDeleteDialogComponent } from 'app/entities/portal-lint-rule-config/portal-lint-rule-config-delete-dialog.component';
import { PortalLintRuleConfigService } from 'app/entities/portal-lint-rule-config/portal-lint-rule-config.service';

describe('Component Tests', () => {
  describe('PortalLintRuleConfig Management Delete Component', () => {
    let comp: PortalLintRuleConfigDeleteDialogComponent;
    let fixture: ComponentFixture<PortalLintRuleConfigDeleteDialogComponent>;
    let service: PortalLintRuleConfigService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalLintRuleConfigDeleteDialogComponent],
      })
        .overrideTemplate(PortalLintRuleConfigDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PortalLintRuleConfigDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalLintRuleConfigService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
