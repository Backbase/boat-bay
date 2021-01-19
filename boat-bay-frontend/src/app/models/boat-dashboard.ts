import { BoatLintReport } from "./boat-lint-report";


export interface BoatStatistics {
  updatedOn: Date;
  mustViolationsCount: number;
  shouldViolationsCount: number;
  mayViolationsCount: number;
  hintViolationsCount: number;
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
