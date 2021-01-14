import { BoatLintReport } from '../lint-report';
import { BoatStatistics } from './boat-dashboard';
import { BoatCapability } from './boat-capability';

export interface BoatProductDashboard {
  portalKey: string;
  portalName: string;

  id: number;
  key: string;
  name: string;
  content: number;

  createdOn: Date;
  createdBy: String;

  capabilities: BoatCapability[];

  lastLintReport: BoatLintReport;

  statistics: BoatStatistics;
}
