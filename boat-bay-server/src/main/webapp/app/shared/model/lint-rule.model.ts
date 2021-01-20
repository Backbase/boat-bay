import { IPortal } from 'app/shared/model/portal.model';
import { Severity } from 'app/shared/model/enumerations/severity.model';

export interface ILintRule {
  id?: number;
  ruleId?: string;
  title?: string;
  ruleSet?: string;
  summary?: string;
  severity?: Severity;
  description?: string;
  externalUrl?: string;
  enabled?: boolean;
  portal?: IPortal;
}

export class LintRule implements ILintRule {
  constructor(
    public id?: number,
    public ruleId?: string,
    public title?: string,
    public ruleSet?: string,
    public summary?: string,
    public severity?: Severity,
    public description?: string,
    public externalUrl?: string,
    public enabled?: boolean,
    public portal?: IPortal
  ) {
    this.enabled = this.enabled || false;
  }
}
