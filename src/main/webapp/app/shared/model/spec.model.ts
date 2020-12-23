import { Moment } from 'moment';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { IService } from 'app/shared/model/service.model';

export interface ISpec {
  id?: number;
  key?: string;
  title?: string;
  openApiUrl?: string;
  boatDocUrl?: string;
  openApi?: string;
  createdOn?: Moment;
  createdBy?: string;
  lintReport?: ILintReport;
  service?: IService;
}

export class Spec implements ISpec {
  constructor(
    public id?: number,
    public key?: string,
    public title?: string,
    public openApiUrl?: string,
    public boatDocUrl?: string,
    public openApi?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public lintReport?: ILintReport,
    public service?: IService
  ) {}
}
