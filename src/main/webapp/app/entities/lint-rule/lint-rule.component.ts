import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintRule } from 'app/shared/model/lint-rule.model';
import { LintRuleService } from './lint-rule.service';
import { LintRuleDeleteDialogComponent } from './lint-rule-delete-dialog.component';

@Component({
  selector: 'jhi-lint-rule',
  templateUrl: './lint-rule.component.html',
})
export class LintRuleComponent implements OnInit, OnDestroy {
  lintRules?: ILintRule[];
  eventSubscriber?: Subscription;

  constructor(protected lintRuleService: LintRuleService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.lintRuleService.query().subscribe((res: HttpResponse<ILintRule[]>) => (this.lintRules = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLintRules();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILintRule): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLintRules(): void {
    this.eventSubscriber = this.eventManager.subscribe('lintRuleListModification', () => this.loadAll());
  }

  delete(lintRule: ILintRule): void {
    const modalRef = this.modalService.open(LintRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lintRule = lintRule;
  }
}
