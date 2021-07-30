import { ILintRule } from 'app/entities/lint-rule/lint-rule.model';
import { ILintReport } from 'app/entities/lint-report/lint-report.model';
import { Severity } from 'app/entities/enumerations/severity.model';

export interface ILintRuleViolation {
  id?: number;
  name?: string;
  description?: string;
  url?: string | null;
  severity?: Severity | null;
  lineStart?: number | null;
  lineEnd?: number | null;
  jsonPointer?: string | null;
  lintRule?: ILintRule;
  lintReport?: ILintReport | null;
}

export class LintRuleViolation implements ILintRuleViolation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public url?: string | null,
    public severity?: Severity | null,
    public lineStart?: number | null,
    public lineEnd?: number | null,
    public jsonPointer?: string | null,
    public lintRule?: ILintRule,
    public lintReport?: ILintReport | null
  ) {}
}

export function getLintRuleViolationIdentifier(lintRuleViolation: ILintRuleViolation): number | undefined {
  return lintRuleViolation.id;
}
