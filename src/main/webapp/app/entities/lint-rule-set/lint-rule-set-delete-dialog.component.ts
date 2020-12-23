import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';
import { LintRuleSetService } from './lint-rule-set.service';

@Component({
  templateUrl: './lint-rule-set-delete-dialog.component.html',
})
export class LintRuleSetDeleteDialogComponent {
  lintRuleSet?: ILintRuleSet;

  constructor(
    protected lintRuleSetService: LintRuleSetService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lintRuleSetService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lintRuleSetListModification');
      this.activeModal.close();
    });
  }
}
