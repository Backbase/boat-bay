import { BoatLintReport } from '../lint-report';
import { BoatStatistics } from './boat-dashboard';

export interface BoatCapability {
  id: number;
  key: string;
  name: string;
  content: string;

  createdOn: Date;
  createdBy: String;

  lastLintReport: BoatLintReport;

  statistics: BoatStatistics;
}
