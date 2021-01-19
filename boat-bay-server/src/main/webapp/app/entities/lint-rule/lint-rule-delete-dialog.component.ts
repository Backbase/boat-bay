import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from './lint-rule.service';

@Component({
  templateUrl: './lint-rule-delete-dialog.component.html',
})
export class LintRuleDeleteDialogComponent {
  lintRule?: ILintRule;

  constructor(protected lintRuleService: LintRuleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintRuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lintRuleListModification');
      this.activeModal.close();
    });
  }
}
