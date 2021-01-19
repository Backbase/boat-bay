import { BoatStatistics } from './boat-dashboard';
import { BoatLintReport } from "./boat-lint-report";

export interface BoatCapability {
  id: number;
  key: string;
  name: string;
  content?: string;

  createdOn: Date;
  createdBy: String;

  lastLintReport?: BoatLintReport;

  statistics: BoatStatistics;
}
