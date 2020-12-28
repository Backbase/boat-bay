import { ILintRule } from 'app/shared/model/lint-rule.model';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

export interface ILintRuleViolation {
  id?: number;
  name?: string;
  description?: string;
  severity?: Severity;
  lineStart?: number;
  lindEnd?: number;
  columnStart?: number;
  columnEnd?: number;
  jsonPointer?: string;
  lintRule?: ILintRule;
  lintReports?: ILintReport[];
}

export class LintRuleViolation implements ILintRuleViolation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public severity?: Severity,
    public lineStart?: number,
    public lindEnd?: number,
    public columnStart?: number,
    public columnEnd?: number,
    public jsonPointer?: string,
    public lintRule?: ILintRule,
    public lintReports?: ILintReport[]
  ) {}
}
