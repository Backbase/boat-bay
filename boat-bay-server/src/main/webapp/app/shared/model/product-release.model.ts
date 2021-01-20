import { Moment } from 'moment';
import { ISpec } from 'app/shared/model/spec.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IProductRelease {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  releaseDate?: Moment;
  hide?: boolean;
  specs?: ISpec[];
  product?: IProduct;
}

export class ProductRelease implements IProductRelease {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public releaseDate?: Moment,
    public hide?: boolean,
    public specs?: ISpec[],
    public product?: IProduct
  ) {
    this.hide = this.hide || false;
  }
}
