import { Moment } from 'moment';
import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface ICapability {
  id?: number;
  key?: string;
  name?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: string;
  version?: string;
  createdOn?: Moment;
  createdBy?: string;
  capabilityServiceDefinitions?: ICapabilityServiceDefinition[];
  portal?: IPortal;
}

export class Capability implements ICapability {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: string,
    public version?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public capabilityServiceDefinitions?: ICapabilityServiceDefinition[],
    public portal?: IPortal
  ) {}
}
