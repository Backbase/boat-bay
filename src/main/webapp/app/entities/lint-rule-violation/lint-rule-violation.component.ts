import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { LintRuleViolationService } from './lint-rule-violation.service';
import { LintRuleViolationDeleteDialogComponent } from './lint-rule-violation-delete-dialog.component';

@Component({
  selector: 'jhi-lint-rule-violation',
  templateUrl: './lint-rule-violation.component.html',
})
export class LintRuleViolationComponent implements OnInit, OnDestroy {
  lintRuleViolations?: ILintRuleViolation[];
  eventSubscriber?: Subscription;

  constructor(
    protected lintRuleViolationService: LintRuleViolationService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.lintRuleViolationService
      .query()
      .subscribe((res: HttpResponse<ILintRuleViolation[]>) => (this.lintRuleViolations = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLintRuleViolations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILintRuleViolation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLintRuleViolations(): void {
    this.eventSubscriber = this.eventManager.subscribe('lintRuleViolationListModification', () => this.loadAll());
  }

  delete(lintRuleViolation: ILintRuleViolation): void {
    const modalRef = this.modalService.open(LintRuleViolationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lintRuleViolation = lintRuleViolation;
  }
}
