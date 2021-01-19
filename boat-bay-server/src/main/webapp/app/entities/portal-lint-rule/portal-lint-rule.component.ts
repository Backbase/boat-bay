import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';
import { PortalLintRuleService } from './portal-lint-rule.service';
import { PortalLintRuleDeleteDialogComponent } from './portal-lint-rule-delete-dialog.component';

@Component({
  selector: 'jhi-portal-lint-rule',
  templateUrl: './portal-lint-rule.component.html',
})
export class PortalLintRuleComponent implements OnInit, OnDestroy {
  portalLintRules?: IPortalLintRule[];
  eventSubscriber?: Subscription;

  constructor(
    protected portalLintRuleService: PortalLintRuleService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.portalLintRuleService.query().subscribe((res: HttpResponse<IPortalLintRule[]>) => (this.portalLintRules = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPortalLintRules();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPortalLintRule): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPortalLintRules(): void {
    this.eventSubscriber = this.eventManager.subscribe('portalLintRuleListModification', () => this.loadAll());
  }

  delete(portalLintRule: IPortalLintRule): void {
    const modalRef = this.modalService.open(PortalLintRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.portalLintRule = portalLintRule;
  }
}
