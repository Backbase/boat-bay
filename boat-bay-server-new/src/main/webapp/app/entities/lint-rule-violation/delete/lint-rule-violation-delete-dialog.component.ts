import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintRuleViolation } from '../lint-rule-violation.model';
import { LintRuleViolationService } from '../service/lint-rule-violation.service';

@Component({
  templateUrl: './lint-rule-violation-delete-dialog.component.html',
})
export class LintRuleViolationDeleteDialogComponent {
  lintRuleViolation?: ILintRuleViolation;

  constructor(protected lintRuleViolationService: LintRuleViolationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintRuleViolationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
