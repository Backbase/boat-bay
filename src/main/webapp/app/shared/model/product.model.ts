import { Moment } from 'moment';
import { ICapability } from 'app/shared/model/capability.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface IProduct {
  id?: number;
  key?: string;
  name?: string;
  order?: number;
  title?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  hide?: boolean;
  capabilities?: ICapability[];
  portal?: IPortal;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number,
    public title?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public hide?: boolean,
    public capabilities?: ICapability[],
    public portal?: IPortal
  ) {
    this.hide = this.hide || false;
  }
}
