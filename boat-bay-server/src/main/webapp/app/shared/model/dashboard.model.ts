import { IPortal } from 'app/shared/model/portal.model';

export interface IDashboard {
  id?: number;
  name?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: any;
  defaultPortal?: IPortal;
}

export class Dashboard implements IDashboard {
  constructor(
    public id?: number,
    public name?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: any,
    public defaultPortal?: IPortal
  ) {}
}
