import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';

@Component({
  selector: 'jhi-portal-lint-rule-config-detail',
  templateUrl: './portal-lint-rule-config-detail.component.html',
})
export class PortalLintRuleConfigDetailComponent implements OnInit {
  portalLintRuleConfig: IPortalLintRuleConfig | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portalLintRuleConfig }) => (this.portalLintRuleConfig = portalLintRuleConfig));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
