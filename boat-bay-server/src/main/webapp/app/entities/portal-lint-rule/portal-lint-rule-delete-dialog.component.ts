import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from './portal-lint-rule.service';

@Component({
  templateUrl: './portal-lint-rule-delete-dialog.component.html',
})
export class PortalLintRuleDeleteDialogComponent {
  portalLintRule?: IPortalLintRule;

  constructor(
    protected portalLintRuleService: PortalLintRuleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portalLintRuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('portalLintRuleListModification');
      this.activeModal.close();
    });
  }
}
