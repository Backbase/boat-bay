import * as dayjs from 'dayjs';
import { IPortal } from 'app/entities/portal/portal.model';
import { ICapability } from 'app/entities/capability/capability.model';
import { IProduct } from 'app/entities/product/product.model';
import { ISource } from 'app/entities/source/source.model';
import { ISpecType } from 'app/entities/spec-type/spec-type.model';
import { ITag } from 'app/entities/tag/tag.model';
import { ILintReport } from 'app/entities/lint-report/lint-report.model';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { IProductRelease } from 'app/entities/product-release/product-release.model';
import { Changes } from 'app/entities/enumerations/changes.model';

export interface ISpec {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  title?: string | null;
  icon?: string | null;
  openApi?: string;
  description?: string | null;
  createdOn?: dayjs.Dayjs;
  createdBy?: string;
  checksum?: string;
  filename?: string;
  valid?: boolean;
  order?: number | null;
  parseError?: string | null;
  externalDocs?: string | null;
  hide?: boolean | null;
  grade?: string | null;
  changes?: Changes | null;
  sourcePath?: string | null;
  sourceName?: string | null;
  sourceUrl?: string | null;
  sourceCreatedBy?: string | null;
  sourceCreatedOn?: dayjs.Dayjs | null;
  sourceLastModifiedOn?: dayjs.Dayjs | null;
  sourceLastModifiedBy?: string | null;
  mvnGroupId?: string | null;
  mvnArtifactId?: string | null;
  mvnVersion?: string | null;
  mvnClassifier?: string | null;
  mvnExtension?: string | null;
  previousSpec?: ISpec | null;
  portal?: IPortal;
  capability?: ICapability | null;
  product?: IProduct;
  source?: ISource | null;
  specType?: ISpecType | null;
  tags?: ITag[] | null;
  lintReport?: ILintReport | null;
  successor?: ISpec | null;
  serviceDefinition?: IServiceDefinition;
  productReleases?: IProductRelease[] | null;
}

export class Spec implements ISpec {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public title?: string | null,
    public icon?: string | null,
    public openApi?: string,
    public description?: string | null,
    public createdOn?: dayjs.Dayjs,
    public createdBy?: string,
    public checksum?: string,
    public filename?: string,
    public valid?: boolean,
    public order?: number | null,
    public parseError?: string | null,
    public externalDocs?: string | null,
    public hide?: boolean | null,
    public grade?: string | null,
    public changes?: Changes | null,
    public sourcePath?: string | null,
    public sourceName?: string | null,
    public sourceUrl?: string | null,
    public sourceCreatedBy?: string | null,
    public sourceCreatedOn?: dayjs.Dayjs | null,
    public sourceLastModifiedOn?: dayjs.Dayjs | null,
    public sourceLastModifiedBy?: string | null,
    public mvnGroupId?: string | null,
    public mvnArtifactId?: string | null,
    public mvnVersion?: string | null,
    public mvnClassifier?: string | null,
    public mvnExtension?: string | null,
    public previousSpec?: ISpec | null,
    public portal?: IPortal,
    public capability?: ICapability | null,
    public product?: IProduct,
    public source?: ISource | null,
    public specType?: ISpecType | null,
    public tags?: ITag[] | null,
    public lintReport?: ILintReport | null,
    public successor?: ISpec | null,
    public serviceDefinition?: IServiceDefinition,
    public productReleases?: IProductRelease[] | null
  ) {
    this.valid = this.valid ?? false;
    this.hide = this.hide ?? false;
  }
}

export function getSpecIdentifier(spec: ISpec): number | undefined {
  return spec.id;
}
