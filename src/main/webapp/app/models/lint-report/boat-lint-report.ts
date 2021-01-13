import { Moment } from 'moment';

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
  lintedOn: Moment;
  version: string;
  openApi: string;

  violations: BoatViolation[];
}
