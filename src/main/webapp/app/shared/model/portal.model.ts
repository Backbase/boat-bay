import { Moment } from 'moment';
import { ICapability } from 'app/shared/model/capability.model';
import { IPortalLintRuleSet } from 'app/shared/model/portal-lint-rule-set.model';

export interface IPortal {
  id?: number;
  key?: string;
  name?: string;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  logoUrl?: string;
  logoLink?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  capabilities?: ICapability[];
  portalRuleSet?: IPortalLintRuleSet;
}

export class Portal implements IPortal {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public logoUrl?: string,
    public logoLink?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public capabilities?: ICapability[],
    public portalRuleSet?: IPortalLintRuleSet
  ) {}
}
