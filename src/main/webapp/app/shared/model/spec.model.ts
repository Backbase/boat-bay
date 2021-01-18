import { Moment } from 'moment';
import { IPortal } from 'app/shared/model/portal.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IProduct } from 'app/shared/model/product.model';
import { ISource } from 'app/shared/model/source.model';
import { ISpecType } from 'app/shared/model/spec-type.model';
import { ITag } from 'app/shared/model/tag.model';
import { ILintReport } from 'app/shared/model/lint-report.model';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { IProductRelease } from 'app/shared/model/product-release.model';

export interface ISpec {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  title?: string;
  icon?: string;
  openApi?: any;
  description?: any;
  createdOn?: Moment;
  createdBy?: string;
  checksum?: string;
  filename?: string;
  valid?: boolean;
  order?: number;
  parseError?: any;
  externalDocs?: string;
  hide?: boolean;
  grade?: string;
  backwardsCompatible?: boolean;
  changed?: boolean;
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
  tags?: ITag[];
  lintReport?: ILintReport;
  serviceDefinition?: IServiceDefinition;
  productReleases?: IProductRelease[];
}

export class Spec implements ISpec {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public title?: string,
    public icon?: string,
    public openApi?: any,
    public description?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public checksum?: string,
    public filename?: string,
    public valid?: boolean,
    public order?: number,
    public parseError?: any,
    public externalDocs?: string,
    public hide?: boolean,
    public grade?: string,
    public backwardsCompatible?: boolean,
    public changed?: boolean,
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
    public tags?: ITag[],
    public lintReport?: ILintReport,
    public serviceDefinition?: IServiceDefinition,
    public productReleases?: IProductRelease[]
  ) {
    this.valid = this.valid || false;
    this.hide = this.hide || false;
    this.backwardsCompatible = this.backwardsCompatible || false;
    this.changed = this.changed || false;
  }
}
