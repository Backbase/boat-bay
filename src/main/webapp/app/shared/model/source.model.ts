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
  filter?: string;
  username?: string;
  password?: string;
  cronExpression?: string;
  specFilterSpEL?: string;
  capabilityKeySpEL?: string;
  capabilityNameSpEL?: string;
  serviceKeySpEL?: string;
  serviceNameSpEL?: string;
  versionSpEL?: string;
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
    public filter?: string,
    public username?: string,
    public password?: string,
    public cronExpression?: string,
    public specFilterSpEL?: string,
    public capabilityKeySpEL?: string,
    public capabilityNameSpEL?: string,
    public serviceKeySpEL?: string,
    public serviceNameSpEL?: string,
    public versionSpEL?: string,
    public overwriteChanges?: boolean,
    public sourcePaths?: ISourcePath[],
    public portal?: IPortal,
    public product?: IProduct,
    public capability?: ICapability,
    public serviceDefinition?: IServiceDefinition
  ) {
    this.active = this.active || false;
    this.overwriteChanges = this.overwriteChanges || false;
  }
}
