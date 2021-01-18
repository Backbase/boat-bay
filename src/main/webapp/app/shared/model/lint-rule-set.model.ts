import { ILintRule } from 'app/shared/model/lint-rule.model';

export interface ILintRuleSet {
  id?: number;
  ruleSetId?: string;
  name?: string;
  description?: any;
  externalUrl?: string;
  lintRules?: ILintRule[];
}

export class LintRuleSet implements ILintRuleSet {
  constructor(
    public id?: number,
    public ruleSetId?: string,
    public name?: string,
    public description?: any,
    public externalUrl?: string,
    public lintRules?: ILintRule[]
  ) {}
}
