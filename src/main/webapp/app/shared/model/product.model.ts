import { Moment } from 'moment';
import { ICapability } from 'app/shared/model/capability.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface IProduct {
  id?: number;
  key?: string;
  name?: string;
  title?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  capabilities?: ICapability[];
  portal?: IPortal;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public title?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public capabilities?: ICapability[],
    public portal?: IPortal
  ) {}
}
