import { BoatLintReport } from '../lint-report';

interface IssueCount {
  severity: string;
  numberOfIssues: number;
}

export interface BoatPortal {
  portalId: number;
  portalKey: string;
  portalName: string;
  productId: number;
  productKey: string;
  productName: string;
  lastLintReport: BoatLintReport;
  issues: IssueCount[];
}
