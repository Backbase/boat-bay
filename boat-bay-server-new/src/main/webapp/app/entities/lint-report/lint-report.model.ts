import * as dayjs from 'dayjs';
import { ISpec } from 'app/entities/spec/spec.model';
import { ILintRuleViolation } from 'app/entities/lint-rule-violation/lint-rule-violation.model';

export interface ILintReport {
  id?: number;
  name?: string | null;
  grade?: string | null;
  passed?: boolean | null;
  lintedOn?: dayjs.Dayjs | null;
  spec?: ISpec | null;
  violations?: ILintRuleViolation[] | null;
}

export class LintReport implements ILintReport {
  constructor(
    public id?: number,
    public name?: string | null,
    public grade?: string | null,
    public passed?: boolean | null,
    public lintedOn?: dayjs.Dayjs | null,
    public spec?: ISpec | null,
    public violations?: ILintRuleViolation[] | null
  ) {
    this.passed = this.passed ?? false;
  }
}

export function getLintReportIdentifier(lintReport: ILintReport): number | undefined {
  return lintReport.id;
}
