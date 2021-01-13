import { Moment } from 'moment';
import { IProductRelease } from 'app/shared/model/product-release.model';
import { IProduct } from 'app/shared/model/product.model';
import { IPortalLintRule } from 'app/shared/model/portal-lint-rule.model';

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
  productReleases?: IProductRelease[];
  products?: IProduct[];
  portalLintRules?: IPortalLintRule[];
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
    public productReleases?: IProductRelease[],
    public products?: IProduct[],
    public portalLintRules?: IPortalLintRule[]
  ) {
    this.hide = this.hide || false;
  }
}
