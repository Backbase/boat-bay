import { IPortal } from 'app/shared/model/portal.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { SourceType } from 'app/shared/model/enumerations/source-type.model';

export interface ISource {
  id?: number;
  name?: string;
  type?: SourceType;
  baseUrl?: string;
  active?: boolean;
  path?: string;
  filter?: string;
  username?: string;
  password?: string;
  cronExpression?: string;
  productKeySpEL?: string;
  productNameSpEL?: string;
  capabilityKeySpEL?: string;
  capabilityNameSpEL?: string;
  serviceKeySpEL?: string;
  serviceNameSpEL?: string;
  versionSpEL?: string;
  overwriteChanges?: boolean;
  portal?: IPortal;
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
    public path?: string,
    public filter?: string,
    public username?: string,
    public password?: string,
    public cronExpression?: string,
    public productKeySpEL?: string,
    public productNameSpEL?: string,
    public capabilityKeySpEL?: string,
    public capabilityNameSpEL?: string,
    public serviceKeySpEL?: string,
    public serviceNameSpEL?: string,
    public versionSpEL?: string,
    public overwriteChanges?: boolean,
    public portal?: IPortal,
    public capability?: ICapability,
    public serviceDefinition?: IServiceDefinition
  ) {
    this.active = this.active || false;
    this.overwriteChanges = this.overwriteChanges || false;
  }
}
