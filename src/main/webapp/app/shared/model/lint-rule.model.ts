import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { ILintRuleSet } from 'app/shared/model/lint-rule-set.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

export interface ILintRule {
  id?: number;
  ruleId?: string;
  title?: string;
  summary?: string;
  severity?: Severity;
  description?: string;
  externalUrl?: string;
  enabled?: boolean;
  lintRuleViolation?: ILintRuleViolation;
  ruleSet?: ILintRuleSet;
}

export class LintRule implements ILintRule {
  constructor(
    public id?: number,
    public ruleId?: string,
    public title?: string,
    public summary?: string,
    public severity?: Severity,
    public description?: string,
    public externalUrl?: string,
    public enabled?: boolean,
    public lintRuleViolation?: ILintRuleViolation,
    public ruleSet?: ILintRuleSet
  ) {
    this.enabled = this.enabled || false;
  }
}
