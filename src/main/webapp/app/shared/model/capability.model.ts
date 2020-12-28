import { Moment } from 'moment';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface ICapability {
  id?: number;
  key?: string;
  name?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: any;
  version?: string;
  createdOn?: Moment;
  createdBy?: string;
  serviceDefinitions?: IServiceDefinition[];
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
    public content?: any,
    public version?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public serviceDefinitions?: IServiceDefinition[],
    public portal?: IPortal
  ) {}
}
