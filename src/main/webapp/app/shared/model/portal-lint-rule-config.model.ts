import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';

export interface IPortalLintRuleConfig {
  id?: number;
  path?: string;
  type?: string;
  value?: any;
  portalLintRule?: IPortalLintRule;
}

export class PortalLintRuleConfig implements IPortalLintRuleConfig {
  constructor(
    public id?: number,
    public path?: string,
    public type?: string,
    public value?: any,
    public portalLintRule?: IPortalLintRule
  ) {}
}
