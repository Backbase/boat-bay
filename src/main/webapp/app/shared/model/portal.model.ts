import { Moment } from 'moment';
import { ICapability } from 'app/shared/model/capability.model';
import { IService } from 'app/shared/model/service.model';

export interface IPortal {
  id?: number;
  key?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  logoUrl?: string;
  logoLink?: string;
  content?: string;
  createdOn?: Moment;
  createdBy?: string;
  capabilities?: ICapability[];
  services?: IService[];
}

export class Portal implements IPortal {
  constructor(
    public id?: number,
    public key?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public logoUrl?: string,
    public logoLink?: string,
    public content?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public capabilities?: ICapability[],
    public services?: IService[]
  ) {}
}
