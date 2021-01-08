import { ILintRule } from 'app/shared/model/lint-rule.model';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

export interface ILintRuleViolation {
  id?: number;
  name?: string;
  description?: any;
  url?: string;
  severity?: Severity;
  lineStart?: number;
  lineEnd?: number;
  jsonPointer?: string;
  lintRule?: ILintRule;
  lintReport?: ILintReport;
}

export class LintRuleViolation implements ILintRuleViolation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public url?: string,
    public severity?: Severity,
    public lineStart?: number,
    public lineEnd?: number,
    public jsonPointer?: string,
    public lintRule?: ILintRule,
    public lintReport?: ILintReport
  ) {}
}
