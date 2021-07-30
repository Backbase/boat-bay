import * as dayjs from 'dayjs';
import { ISourcePath } from 'app/entities/source-path/source-path.model';
import { IPortal } from 'app/entities/portal/portal.model';
import { IProduct } from 'app/entities/product/product.model';
import { ICapability } from 'app/entities/capability/capability.model';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { SourceType } from 'app/entities/enumerations/source-type.model';

export interface ISource {
  id?: number;
  name?: string;
  key?: string;
  type?: SourceType;
  baseUrl?: string;
  active?: boolean | null;
  filterArtifactsName?: string | null;
  filterArtifactsCreatedSince?: dayjs.Dayjs | null;
  username?: string | null;
  password?: string | null;
  cronExpression?: string | null;
  runOnStartup?: boolean | null;
  specFilterSpEL?: string | null;
  capabilityKeySpEL?: string | null;
  capabilityNameSpEL?: string | null;
  serviceKeySpEL?: string | null;
  serviceNameSpEL?: string | null;
  specKeySpEL?: string | null;
  versionSpEL?: string | null;
  productReleaseNameSpEL?: string | null;
  productReleaseVersionSpEL?: string | null;
  productReleaseKeySpEL?: string | null;
  itemLimit?: number | null;
  overwriteChanges?: boolean | null;
  options?: string | null;
  sourcePaths?: ISourcePath[] | null;
  portal?: IPortal;
  product?: IProduct;
  capability?: ICapability | null;
  serviceDefinition?: IServiceDefinition | null;
}

export class Source implements ISource {
  constructor(
    public id?: number,
    public name?: string,
    public key?: string,
    public type?: SourceType,
    public baseUrl?: string,
    public active?: boolean | null,
    public filterArtifactsName?: string | null,
    public filterArtifactsCreatedSince?: dayjs.Dayjs | null,
    public username?: string | null,
    public password?: string | null,
    public cronExpression?: string | null,
    public runOnStartup?: boolean | null,
    public specFilterSpEL?: string | null,
    public capabilityKeySpEL?: string | null,
    public capabilityNameSpEL?: string | null,
    public serviceKeySpEL?: string | null,
    public serviceNameSpEL?: string | null,
    public specKeySpEL?: string | null,
    public versionSpEL?: string | null,
    public productReleaseNameSpEL?: string | null,
    public productReleaseVersionSpEL?: string | null,
    public productReleaseKeySpEL?: string | null,
    public itemLimit?: number | null,
    public overwriteChanges?: boolean | null,
    public options?: string | null,
    public sourcePaths?: ISourcePath[] | null,
    public portal?: IPortal,
    public product?: IProduct,
    public capability?: ICapability | null,
    public serviceDefinition?: IServiceDefinition | null
  ) {
    this.active = this.active ?? false;
    this.runOnStartup = this.runOnStartup ?? false;
    this.overwriteChanges = this.overwriteChanges ?? false;
  }
}

export function getSourceIdentifier(source: ISource): number | undefined {
  return source.id;
}
