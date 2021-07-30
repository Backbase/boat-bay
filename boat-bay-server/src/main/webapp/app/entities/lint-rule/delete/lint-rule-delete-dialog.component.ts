import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintRule } from '../lint-rule.model';
import { LintRuleService } from '../service/lint-rule.service';

@Component({
  templateUrl: './lint-rule-delete-dialog.component.html',
})
export class LintRuleDeleteDialogComponent {
  lintRule?: ILintRule;

  constructor(protected lintRuleService: LintRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
