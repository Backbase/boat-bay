import { Moment } from 'moment';
import { IProductRelease } from 'app/shared/model/product-release.model';
import { IProduct } from 'app/shared/model/product.model';
import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

export interface IPortal {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  logoUrl?: string;
  logoLink?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  hide?: boolean;
  productReleases?: IProductRelease[];
  products?: IProduct[];
  portalRuleSet?: IPortalLintRuleSet;
}

export class Portal implements IPortal {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public logoUrl?: string,
    public logoLink?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public hide?: boolean,
    public productReleases?: IProductRelease[],
    public products?: IProduct[],
    public portalRuleSet?: IPortalLintRuleSet
  ) {
    this.hide = this.hide || false;
  }
}
