import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDashboard } from 'app/shared/model/dashboard.model';
import { DashboardService } from './dashboard.service';

@Component({
  templateUrl: './dashboard-delete-dialog.component.html',
})
export class DashboardDeleteDialogComponent {
  dashboard?: IDashboard;

  constructor(protected dashboardService: DashboardService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dashboardService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dashboardListModification');
      this.activeModal.close();
    });
  }
}
