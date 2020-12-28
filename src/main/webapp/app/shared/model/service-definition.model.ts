import { Moment } from 'moment';
import { ICapability } from 'app/shared/model/capability.model';

export interface IServiceDefinition {
  id?: number;
  key?: string;
  name?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: string;
  createdOn?: Moment;
  createdBy?: string;
  capability?: ICapability;
}

export class ServiceDefinition implements IServiceDefinition {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public capability?: ICapability
  ) {}
}
