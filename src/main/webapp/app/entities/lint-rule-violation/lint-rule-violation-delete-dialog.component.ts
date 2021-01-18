import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from './lint-rule-violation.service';

@Component({
  templateUrl: './lint-rule-violation-delete-dialog.component.html',
})
export class LintRuleViolationDeleteDialogComponent {
  lintRuleViolation?: ILintRuleViolation;

  constructor(
    protected lintRuleViolationService: LintRuleViolationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintRuleViolationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lintRuleViolationListModification');
      this.activeModal.close();
    });
  }
}
