import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BoatBayTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CapabilityServiceDefinitionDeleteDialogComponent } from 'app/entities/capability-service-definition/capability-service-definition-delete-dialog.component';
import { CapabilityServiceDefinitionService } from 'app/entities/capability-service-definition/capability-service-definition.service';

describe('Component Tests', () => {
  describe('CapabilityServiceDefinition Management Delete Component', () => {
    let comp: CapabilityServiceDefinitionDeleteDialogComponent;
    let fixture: ComponentFixture<CapabilityServiceDefinitionDeleteDialogComponent>;
    let service: CapabilityServiceDefinitionService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceDefinitionDeleteDialogComponent],
      })
        .overrideTemplate(CapabilityServiceDefinitionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CapabilityServiceDefinitionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityServiceDefinitionService);
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
