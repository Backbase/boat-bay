import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';
import { PortalLintRuleSetService } from './portal-lint-rule-set.service';

@Component({
  templateUrl: './portal-lint-rule-set-delete-dialog.component.html',
})
export class PortalLintRuleSetDeleteDialogComponent {
  portalLintRuleSet?: IPortalLintRuleSet;

  constructor(
    protected portalLintRuleSetService: PortalLintRuleSetService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portalLintRuleSetService.delete(id).subscribe(() => {
      this.eventManager.broadcast('portalLintRuleSetListModification');
      this.activeModal.close();
    });
  }
}
