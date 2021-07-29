import { IPortal } from 'app/entities/portal/portal.model';
import { Severity } from 'app/entities/enumerations/severity.model';

export interface ILintRule {
  id?: number;
  ruleId?: string;
  title?: string;
  ruleSet?: string | null;
  summary?: string | null;
  severity?: Severity;
  description?: string | null;
  externalUrl?: string | null;
  enabled?: boolean;
  portal?: IPortal;
}

export class LintRule implements ILintRule {
  constructor(
    public id?: number,
    public ruleId?: string,
    public title?: string,
    public ruleSet?: string | null,
    public summary?: string | null,
    public severity?: Severity,
    public description?: string | null,
    public externalUrl?: string | null,
    public enabled?: boolean,
    public portal?: IPortal
  ) {
    this.enabled = this.enabled ?? false;
  }
}

export function getLintRuleIdentifier(lintRule: ILintRule): number | undefined {
  return lintRule.id;
}
