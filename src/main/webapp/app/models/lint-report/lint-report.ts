interface Rule {
  id: number;
  ruleSet: string;
  title: string;
  severity: string;
  ignored: boolean;
  url: string;
  effortMinutes: number;
  type: string;
}

interface Range {
  start: number;
  end: number;
}

interface Pointer {
  matchingProperty: string;
}

export interface Violation {
  rule: Rule;
  description: string;
  severity: string;
  lines: Range;
  pointer: Pointer;
}

export interface LintReport {
  title: string;
  version: string;
  filePath: string;
  openApi: string;
  violations: Violation[];
}
