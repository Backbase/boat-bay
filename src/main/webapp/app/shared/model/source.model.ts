import { Moment } from 'moment';
import { ISourcePath } from 'app/shared/model/source-path.model';
import { IPortal } from 'app/shared/model/portal.model';
import { IProduct } from 'app/shared/model/product.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { SourceType } from 'app/shared/model/enumerations/source-type.model';

export interface ISource {
  id?: number;
  name?: string;
  type?: SourceType;
  baseUrl?: string;
  active?: boolean;
  filterArtifactsName?: string;
  filterArtifactsCreatedSince?: Moment;
  username?: string;
  password?: string;
  cronExpression?: string;
  runOnStartup?: boolean;
  specFilterSpEL?: string;
  capabilityKeySpEL?: string;
  capabilityNameSpEL?: string;
  serviceKeySpEL?: string;
  serviceNameSpEL?: string;
  specKeySpEL?: string;
  versionSpEL?: string;
  productReleaseSpEL?: string;
  itemLimit?: number;
  overwriteChanges?: boolean;
  sourcePaths?: ISourcePath[];
  portal?: IPortal;
  product?: IProduct;
  capability?: ICapability;
  serviceDefinition?: IServiceDefinition;
}

export class Source implements ISource {
  constructor(
    public id?: number,
    public name?: string,
    public type?: SourceType,
    public baseUrl?: string,
    public active?: boolean,
    public filterArtifactsName?: string,
    public filterArtifactsCreatedSince?: Moment,
    public username?: string,
    public password?: string,
    public cronExpression?: string,
    public runOnStartup?: boolean,
    public specFilterSpEL?: string,
    public capabilityKeySpEL?: string,
    public capabilityNameSpEL?: string,
    public serviceKeySpEL?: string,
    public serviceNameSpEL?: string,
    public specKeySpEL?: string,
    public versionSpEL?: string,
    public productReleaseSpEL?: string,
    public itemLimit?: number,
    public overwriteChanges?: boolean,
    public sourcePaths?: ISourcePath[],
    public portal?: IPortal,
    public product?: IProduct,
    public capability?: ICapability,
    public serviceDefinition?: IServiceDefinition
  ) {
    this.active = this.active || false;
    this.runOnStartup = this.runOnStartup || false;
    this.overwriteChanges = this.overwriteChanges || false;
  }
}
