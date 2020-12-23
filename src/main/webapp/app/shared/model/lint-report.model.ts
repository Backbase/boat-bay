import { ISpec } from 'app/shared/model/spec.model';
import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

export interface ILintReport {
  id?: number;
  grade?: string;
  passed?: boolean;
  spec?: ISpec;
  lintRuleViolation?: ILintRuleViolation;
}

export class LintReport implements ILintReport {
  constructor(
    public id?: number,
    public grade?: string,
    public passed?: boolean,
    public spec?: ISpec,
    public lintRuleViolation?: ILintRuleViolation
  ) {
    this.passed = this.passed || false;
  }
}
