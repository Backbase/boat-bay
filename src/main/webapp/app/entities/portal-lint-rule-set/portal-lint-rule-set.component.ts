import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';
import { PortalLintRuleSetService } from './portal-lint-rule-set.service';
import { PortalLintRuleSetDeleteDialogComponent } from './portal-lint-rule-set-delete-dialog.component';

@Component({
  selector: 'jhi-portal-lint-rule-set',
  templateUrl: './portal-lint-rule-set.component.html',
})
export class PortalLintRuleSetComponent implements OnInit, OnDestroy {
  portalLintRuleSets?: IPortalLintRuleSet[];
  eventSubscriber?: Subscription;

  constructor(
    protected portalLintRuleSetService: PortalLintRuleSetService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.portalLintRuleSetService
      .query()
      .subscribe((res: HttpResponse<IPortalLintRuleSet[]>) => (this.portalLintRuleSets = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPortalLintRuleSets();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPortalLintRuleSet): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPortalLintRuleSets(): void {
    this.eventSubscriber = this.eventManager.subscribe('portalLintRuleSetListModification', () => this.loadAll());
  }

  delete(portalLintRuleSet: IPortalLintRuleSet): void {
    const modalRef = this.modalService.open(PortalLintRuleSetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.portalLintRuleSet = portalLintRuleSet;
  }
}
