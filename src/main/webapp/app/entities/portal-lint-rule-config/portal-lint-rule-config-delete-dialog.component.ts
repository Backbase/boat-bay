import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';
import { PortalLintRuleConfigService } from './portal-lint-rule-config.service';

@Component({
  templateUrl: './portal-lint-rule-config-delete-dialog.component.html',
})
export class PortalLintRuleConfigDeleteDialogComponent {
  portalLintRuleConfig?: IPortalLintRuleConfig;

  constructor(
    protected portalLintRuleConfigService: PortalLintRuleConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portalLintRuleConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('portalLintRuleConfigListModification');
      this.activeModal.close();
    });
  }
}
