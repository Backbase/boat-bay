import { ILintRule } from 'app/shared/model/lint-rule.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface IPortalLintRule {
  id?: number;
  ruleId?: string;
  enabled?: boolean;
  lintRule?: ILintRule;
  portal?: IPortal;
}

export class PortalLintRule implements IPortalLintRule {
  constructor(public id?: number, public ruleId?: string, public enabled?: boolean, public lintRule?: ILintRule, public portal?: IPortal) {
    this.enabled = this.enabled || false;
  }
}
