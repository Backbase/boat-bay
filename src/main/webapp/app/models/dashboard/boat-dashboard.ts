import { BoatLintReport } from '../lint-report';

interface IssueCount {
  severity: string;
  numberOfIssues: number;
}

export interface BoatStatistics {
  updatedOn: Date;
  issues: IssueCount[];
}

export interface BoatDashboard {
  portalId: number;
  portalKey: string;
  portalName: string;
  productId: number;
  productKey: string;
  productName: string;
  numberOfServices: number;
  numberOfCapabilities: number;
  lastLintReport: BoatLintReport;
  statistics: BoatStatistics;
}
