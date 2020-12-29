import { IPortal } from 'app/shared/model/portal.model';
import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';

export interface IPortalLintRuleSet {
  id?: number;
  name?: string;
  portal?: IPortal;
  portalLintRules?: IPortalLintRule[];
}

export class PortalLintRuleSet implements IPortalLintRuleSet {
  constructor(public id?: number, public name?: string, public portal?: IPortal, public portalLintRules?: IPortalLintRule[]) {}
}
