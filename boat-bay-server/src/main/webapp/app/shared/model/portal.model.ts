import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';
import { ILintRule } from 'app/shared/model/lint-rule.model';

export interface IPortal {
  id?: number;
  key?: string;
  name?: string;
  subTitle?: string;
  logoUrl?: string;
  logoLink?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  hide?: boolean;
  linted?: boolean;
  products?: IProduct[];
  lintRules?: ILintRule[];
}

export class Portal implements IPortal {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public subTitle?: string,
    public logoUrl?: string,
    public logoLink?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public hide?: boolean,
    public linted?: boolean,
    public products?: IProduct[],
    public lintRules?: ILintRule[]
  ) {
    this.hide = this.hide || false;
    this.linted = this.linted || false;
  }
}
