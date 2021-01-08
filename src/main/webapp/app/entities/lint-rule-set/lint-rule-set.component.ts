import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';
import { LintRuleSetService } from './lint-rule-set.service';

@Component({
  selector: 'jhi-lint-rule-set',
  templateUrl: './lint-rule-set.component.html',
})
export class LintRuleSetComponent implements OnInit, OnDestroy {
  lintRuleSets?: ILintRuleSet[];
  eventSubscriber?: Subscription;

  constructor(
    protected lintRuleSetService: LintRuleSetService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager
  ) {}

  loadAll(): void {
    this.lintRuleSetService.query().subscribe((res: HttpResponse<ILintRuleSet[]>) => (this.lintRuleSets = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLintRuleSets();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILintRuleSet): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInLintRuleSets(): void {
    this.eventSubscriber = this.eventManager.subscribe('lintRuleSetListModification', () => this.loadAll());
  }
}
