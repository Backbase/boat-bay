import { Moment } from 'moment';
import { ISpec } from 'app/shared/model/spec.model';
import { ICapability } from 'app/shared/model/capability.model';

export interface IServiceDefinition {
  id?: number;
  key?: string;
  name?: string;
  order?: number;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  description?: any;
  icon?: string;
  color?: string;
  createdOn?: Moment;
  createdBy?: string;
  hide?: boolean;
  specs?: ISpec[];
  capability?: ICapability;
}

export class ServiceDefinition implements IServiceDefinition {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public description?: any,
    public icon?: string,
    public color?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public hide?: boolean,
    public specs?: ISpec[],
    public capability?: ICapability
  ) {
    this.hide = this.hide || false;
  }
}
