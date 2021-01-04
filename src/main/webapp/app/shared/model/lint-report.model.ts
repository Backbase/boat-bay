import { Moment } from 'moment';
import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';
import { ISpec } from 'app/shared/model/spec.model';

export interface ILintReport {
  id?: number;
  name?: string;
  grade?: string;
  passed?: boolean;
  lintedOn?: Moment;
  lintRuleViolations?: ILintRuleViolation[];
  spec?: ISpec;
}

export class LintReport implements ILintReport {
  constructor(
    public id?: number,
    public name?: string,
    public grade?: string,
    public passed?: boolean,
    public lintedOn?: Moment,
    public lintRuleViolations?: ILintRuleViolation[],
    public spec?: ISpec
  ) {
    this.passed = this.passed || false;
  }
}
