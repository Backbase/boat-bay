import { ISpec } from 'app/shared/model/spec.model';
import { IPortal } from 'app/shared/model/portal.model';

export interface IProductRelease {
  id?: number;
  key?: string;
  name?: string;
  hide?: boolean;
  specs?: ISpec[];
  portal?: IPortal;
}

export class ProductRelease implements IProductRelease {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public hide?: boolean,
    public specs?: ISpec[],
    public portal?: IPortal
  ) {
    this.hide = this.hide || false;
  }
}
