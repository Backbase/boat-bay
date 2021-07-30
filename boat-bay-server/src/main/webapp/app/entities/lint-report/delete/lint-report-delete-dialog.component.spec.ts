jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LintReportService } from '../service/lint-report.service';

import { LintReportDeleteDialogComponent } from './lint-report-delete-dialog.component';

describe('Component Tests', () => {
  describe('LintReport Management Delete Component', () => {
    let comp: LintReportDeleteDialogComponent;
    let fixture: ComponentFixture<LintReportDeleteDialogComponent>;
    let service: LintReportService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintReportDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(LintReportDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LintReportDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LintReportService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
