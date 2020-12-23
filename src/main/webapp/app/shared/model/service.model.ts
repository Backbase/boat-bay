import { Moment } from 'moment';
import { ISpec } from 'app/shared/model/spec.model';
import { ICapability } from 'app/shared/model/capability.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface IService {
  id?: number;
  key?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: string;
  createdOn?: Moment;
  createdBy?: string;
  specs?: ISpec[];
  capability?: ICapability;
  portal?: IPortal;
}

export class Service implements IService {
  constructor(
    public id?: number,
    public key?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: string,
    public createdOn?: Moment,
    public createdBy?: string,
    public specs?: ISpec[],
    public capability?: ICapability,
    public portal?: IPortal
  ) {}
}
