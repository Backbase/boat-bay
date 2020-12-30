import { Moment } from 'moment';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { IPortal } from 'app/shared/model/portal.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IProduct } from 'app/shared/model/product.model';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { ISource } from 'app/shared/model/source.model';

export interface ISpec {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  title?: string;
  openApi?: any;
  createdOn?: Moment;
  createdBy?: string;
  checksum?: string;
  filename?: string;
  valid?: boolean;
  parseError?: any;
  sourcePath?: string;
  sourceName?: string;
  sourceUrl?: string;
  sourceCreatedBy?: string;
  sourceCreatedOn?: Moment;
  sourceLastModifiedOn?: Moment;
  sourceLastModifiedBy?: string;
  lintReport?: ILintReport;
  portal?: IPortal;
  capability?: ICapability;
  product?: IProduct;
  serviceDefinition?: IServiceDefinition;
  source?: ISource;
}

export class Spec implements ISpec {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public title?: string,
    public openApi?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public checksum?: string,
    public filename?: string,
    public valid?: boolean,
    public parseError?: any,
    public sourcePath?: string,
    public sourceName?: string,
    public sourceUrl?: string,
    public sourceCreatedBy?: string,
    public sourceCreatedOn?: Moment,
    public sourceLastModifiedOn?: Moment,
    public sourceLastModifiedBy?: string,
    public lintReport?: ILintReport,
    public portal?: IPortal,
    public capability?: ICapability,
    public product?: IProduct,
    public serviceDefinition?: IServiceDefinition,
    public source?: ISource
  ) {
    this.valid = this.valid || false;
  }
}
