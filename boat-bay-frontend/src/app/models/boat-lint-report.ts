import { BoatSpec } from "./boat-spec";

interface Rule {
  id: number;
  ruleId: string;
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

export interface BoatViolation {
  rule: Rule;
  description: string;
  severity: string;
  lines: Range;
  pointer: Pointer;
}

export interface BoatLintReport {
  id: number;
  name: string;
  grade: string;
  passed: boolean;
  lintedOn: Date;
  version: string;
  openApi?: string;
  spec: BoatSpec;
  violations: BoatViolation[];
}
