import { Moment } from 'moment';
import { IService } from 'app/shared/model/service.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface ICapability {
  id?: number;
  key?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: string;
  version?: string;
  createdOn?: Moment;
  createdBy?: string;
  services?: IService[];
  portal?: IPortal;
}

export class Capability implements ICapability {
  constructor(
    public id?: number,
    public key?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: string,
    public version?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public services?: IService[],
    public portal?: IPortal
  ) {}
}
