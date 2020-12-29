import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';
import { PortalLintRuleConfigService } from './portal-lint-rule-config.service';
import { PortalLintRuleConfigDeleteDialogComponent } from './portal-lint-rule-config-delete-dialog.component';

@Component({
  selector: 'jhi-portal-lint-rule-config',
  templateUrl: './portal-lint-rule-config.component.html',
})
export class PortalLintRuleConfigComponent implements OnInit, OnDestroy {
  portalLintRuleConfigs?: IPortalLintRuleConfig[];
  eventSubscriber?: Subscription;

  constructor(
    protected portalLintRuleConfigService: PortalLintRuleConfigService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.portalLintRuleConfigService
      .query()
      .subscribe((res: HttpResponse<IPortalLintRuleConfig[]>) => (this.portalLintRuleConfigs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPortalLintRuleConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPortalLintRuleConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInPortalLintRuleConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('portalLintRuleConfigListModification', () => this.loadAll());
  }

  delete(portalLintRuleConfig: IPortalLintRuleConfig): void {
    const modalRef = this.modalService.open(PortalLintRuleConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.portalLintRuleConfig = portalLintRuleConfig;
  }
}
