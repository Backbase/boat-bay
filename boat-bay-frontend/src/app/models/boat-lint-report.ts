import { BoatSpec } from "./boat-spec";
import { BoatLintRule } from "./boat-lint-rule";

interface Range {
  start: number;
  end: number;
}

interface Pointer {
  matchingProperty: string;
}

export interface BoatViolation {
  rule: BoatLintRule;
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
