import { BoatLintReport } from "./boat-lint-report";
import { BoatStatistics } from "./boat-dashboard";

export interface BoatService {
  id: number;
  key: string;
  name: string;
  content?: string;

  createdOn: Date;
  createdBy: String;

  lastLintReport?: BoatLintReport;

  statistics: BoatStatistics;
}
