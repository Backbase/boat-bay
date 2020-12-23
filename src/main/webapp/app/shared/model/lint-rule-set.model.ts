export interface ILintRuleSet {
  id?: number;
  name?: string;
  description?: string;
}

export class LintRuleSet implements ILintRuleSet {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
