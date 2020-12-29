import { IPortalLintRuleConfig } from 'app/shared/model/portal-lint-rule-config.model';
import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

export interface IPortalLintRule {
  id?: number;
  name?: string;
  portalLintRuleConfig?: IPortalLintRuleConfig;
  portalRuleSet?: IPortalLintRuleSet;
}

export class PortalLintRule implements IPortalLintRule {
  constructor(
    public id?: number,
    public name?: string,
    public portalLintRuleConfig?: IPortalLintRuleConfig,
    public portalRuleSet?: IPortalLintRuleSet
  ) {}
}
