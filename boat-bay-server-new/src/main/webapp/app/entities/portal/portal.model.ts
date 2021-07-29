import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';
import { ILintRule } from 'app/entities/lint-rule/lint-rule.model';
import { IZallyConfig } from 'app/entities/zally-config/zally-config.model';

export interface IPortal {
  id?: number;
  key?: string;
  name?: string;
  subTitle?: string | null;
  logoUrl?: string | null;
  logoLink?: string | null;
  content?: string | null;
  createdOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  hide?: boolean | null;
  linted?: boolean | null;
  products?: IProduct[] | null;
  lintRules?: ILintRule[] | null;
  zallyConfig?: IZallyConfig | null;
}

export class Portal implements IPortal {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public subTitle?: string | null,
    public logoUrl?: string | null,
    public logoLink?: string | null,
    public content?: string | null,
    public createdOn?: dayjs.Dayjs | null,
    public createdBy?: string | null,
    public hide?: boolean | null,
    public linted?: boolean | null,
    public products?: IProduct[] | null,
    public lintRules?: ILintRule[] | null,
    public zallyConfig?: IZallyConfig | null
  ) {
    this.hide = this.hide ?? false;
    this.linted = this.linted ?? false;
  }
}

export function getPortalIdentifier(portal: IPortal): number | undefined {
  return portal.id;
}
