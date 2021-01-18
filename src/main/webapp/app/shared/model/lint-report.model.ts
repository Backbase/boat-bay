import { Moment } from 'moment';
import { ISpec } from 'app/shared/model/spec.model';
import { ILintRuleViolation } from 'app/shared/model/lint-rule-violation.model';

export interface ILintReport {
  id?: number;
  name?: string;
  grade?: string;
  passed?: boolean;
  lintedOn?: Moment;
  spec?: ISpec;
  violations?: ILintRuleViolation[];
}

export class LintReport implements ILintReport {
  constructor(
    public id?: number,
    public name?: string,
    public grade?: string,
    public passed?: boolean,
    public lintedOn?: Moment,
    public spec?: ISpec,
    public violations?: ILintRuleViolation[]
  ) {
    this.passed = this.passed || false;
  }
}
