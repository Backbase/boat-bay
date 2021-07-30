import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintRule } from '../lint-rule.model';
import { LintRuleService } from '../service/lint-rule.service';
import { LintRuleDeleteDialogComponent } from '../delete/lint-rule-delete-dialog.component';

@Component({
  selector: 'jhi-lint-rule',
  templateUrl: './lint-rule.component.html',
})
export class LintRuleComponent implements OnInit {
  lintRules?: ILintRule[];
  isLoading = false;

  constructor(protected lintRuleService: LintRuleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lintRuleService.query().subscribe(
      (res: HttpResponse<ILintRule[]>) => {
        this.isLoading = false;
        this.lintRules = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILintRule): number {
    return item.id!;
  }

  delete(lintRule: ILintRule): void {
    const modalRef = this.modalService.open(LintRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lintRule = lintRule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
