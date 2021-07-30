import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

@Component({
  templateUrl: './dashboard-delete-dialog.component.html',
})
export class DashboardDeleteDialogComponent {
  dashboard?: IDashboard;

  constructor(protected dashboardService: DashboardService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dashboardService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
