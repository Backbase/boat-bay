import { Moment } from 'moment';
import { IPortal } from 'app/shared/model/portal.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IProduct } from 'app/shared/model/product.model';
import { ISource } from 'app/shared/model/source.model';
import { ISpecType } from 'app/shared/model/spec-type.model';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';

export interface ISpec {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  title?: string;
  openApi?: any;
  tagsCsv?: string;
  description?: any;
  createdOn?: Moment;
  createdBy?: string;
  checksum?: string;
  filename?: string;
  valid?: boolean;
  order?: number;
  parseError?: any;
  externalDocs?: string;
  sourcePath?: string;
  sourceName?: string;
  sourceUrl?: string;
  sourceCreatedBy?: string;
  sourceCreatedOn?: Moment;
  sourceLastModifiedOn?: Moment;
  sourceLastModifiedBy?: string;
  portal?: IPortal;
  capability?: ICapability;
  product?: IProduct;
  source?: ISource;
  specType?: ISpecType;
  lintReport?: ILintReport;
  serviceDefinition?: IServiceDefinition;
}

export class Spec implements ISpec {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public title?: string,
    public openApi?: any,
    public tagsCsv?: string,
    public description?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public checksum?: string,
    public filename?: string,
    public valid?: boolean,
    public order?: number,
    public parseError?: any,
    public externalDocs?: string,
    public sourcePath?: string,
    public sourceName?: string,
    public sourceUrl?: string,
    public sourceCreatedBy?: string,
    public sourceCreatedOn?: Moment,
    public sourceLastModifiedOn?: Moment,
    public sourceLastModifiedBy?: string,
    public portal?: IPortal,
    public capability?: ICapability,
    public product?: IProduct,
    public source?: ISource,
    public specType?: ISpecType,
    public lintReport?: ILintReport,
    public serviceDefinition?: IServiceDefinition
  ) {
    this.valid = this.valid || false;
  }
}
