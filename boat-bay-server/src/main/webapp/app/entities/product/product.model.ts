import * as dayjs from 'dayjs';
import { IProductRelease } from 'app/entities/product-release/product-release.model';
import { ICapability } from 'app/entities/capability/capability.model';
import { IPortal } from 'app/entities/portal/portal.model';

export interface IProduct {
  id?: number;
  key?: string;
  name?: string;
  order?: number | null;
  content?: string | null;
  createdOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  hide?: boolean | null;
  jiraProjectId?: string | null;
  productReleases?: IProductRelease[] | null;
  capabilities?: ICapability[] | null;
  portal?: IPortal;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number | null,
    public content?: string | null,
    public createdOn?: dayjs.Dayjs | null,
    public createdBy?: string | null,
    public hide?: boolean | null,
    public jiraProjectId?: string | null,
    public productReleases?: IProductRelease[] | null,
    public capabilities?: ICapability[] | null,
    public portal?: IPortal
  ) {
    this.hide = this.hide ?? false;
  }
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
