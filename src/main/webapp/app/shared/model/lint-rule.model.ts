import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

export interface ILintRule {
  id?: number;
  title?: string;
  summary?: string;
  severity?: Severity;
  description?: string;
  externalUrl?: string;
  enabled?: boolean;
  lintRuleViolation?: ILintRuleViolation;
}

export class LintRule implements ILintRule {
  constructor(
    public id?: number,
    public title?: string,
    public summary?: string,
    public severity?: Severity,
    public description?: string,
    public externalUrl?: string,
    public enabled?: boolean,
    public lintRuleViolation?: ILintRuleViolation
  ) {
    this.enabled = this.enabled || false;
  }
}
