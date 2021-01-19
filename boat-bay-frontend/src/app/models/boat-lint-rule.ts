export interface BoatLintRule {
  id: number;
  ruleSet: string;
  enabled: string;
  title: string;
  severity: string;
  ignored: boolean;
  url: string;
  effortMinutes: number;
  type: string;
}
