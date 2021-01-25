import { BoatLintReport } from './boat-lint-report';
import { BoatStatistics } from './boat-dashboard';
import { BoatCapability } from "./boat-capability";
import { BoatService } from "./boat-service";

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

  backwardsCompatible: boolean;
  changed: boolean;

  capability: BoatCapability;
  serviceDefinition: BoatService;
}
