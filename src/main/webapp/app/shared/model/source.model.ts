import { IPortal } from 'app/shared/model/portal.model';
import { ICapability } from 'app/shared/model/capability.model';
import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { SourceType } from 'app/shared/model/enumerations/source-type.model';

export interface ISource {
  id?: number;
  baseUrl?: string;
  name?: string;
  type?: SourceType;
  active?: boolean;
  path?: string;
  filter?: string;
  username?: string;
  password?: string;
  cronExpression?: string;
  capabilityNameSpEL?: string;
  serviceNameSpEL?: string;
  versionSpEL?: string;
  portal?: IPortal;
  capability?: ICapability;
  capabilityServiceDefinition?: ICapabilityServiceDefinition;
}

export class Source implements ISource {
  constructor(
    public id?: number,
    public baseUrl?: string,
    public name?: string,
    public type?: SourceType,
    public active?: boolean,
    public path?: string,
    public filter?: string,
    public username?: string,
    public password?: string,
    public cronExpression?: string,
    public capabilityNameSpEL?: string,
    public serviceNameSpEL?: string,
    public versionSpEL?: string,
    public portal?: IPortal,
    public capability?: ICapability,
    public capabilityServiceDefinition?: ICapabilityServiceDefinition
  ) {
    this.active = this.active || false;
  }
}
