import { BoatLintReport } from './boat-lint-report';
import { BoatStatistics } from './boat-dashboard';
import { BoatCapability } from "./boat-capability";
import { BoatService } from "./boat-service";

export enum Changes {
  NOT_APPLICABLE= 'NOT_APPLICABLE', ERROR_COMPARING = 'ERROR_COMPARING', UNCHANGED = "UNCHANGED", COMPATIBLE = "COMPATIBLE", BREAKING = "BREAKING"
}

export interface BoatSpec {

  id: number;
  key: string;
  name: string;
  content?: string;
  openApi?: string;

  createdOn: Date;
  createdBy: String;

  lastLintReport?: BoatLintReport;

  statistics: BoatStatistics;

  version: string;
  title: string;
  description: string;
  icon: string;
  grade: string;

  changes: Changes;

  capability: BoatCapability;
  serviceDefinition: BoatService;
}
