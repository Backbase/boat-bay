import { BoatLintReport } from "./boat-lint-report";
import { BoatStatistics } from "./boat-dashboard";

export interface BoatProduct {
  portalKey: string;
  portalName: string;

  id: number;
  key: string;
  name: string;
  content: number;

  createdOn: Date;
  createdBy: String;

  lastLintReport: BoatLintReport;

  statistics: BoatStatistics;

  jiraProjectId: string;
}
