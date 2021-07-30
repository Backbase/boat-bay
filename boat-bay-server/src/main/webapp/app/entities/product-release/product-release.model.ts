import * as dayjs from 'dayjs';
import { ISpec } from 'app/entities/spec/spec.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductRelease {
  id?: number;
  key?: string;
  name?: string;
  version?: string;
  releaseDate?: dayjs.Dayjs | null;
  hide?: boolean | null;
  specs?: ISpec[] | null;
  product?: IProduct;
}

export class ProductRelease implements IProductRelease {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public version?: string,
    public releaseDate?: dayjs.Dayjs | null,
    public hide?: boolean | null,
    public specs?: ISpec[] | null,
    public product?: IProduct
  ) {
    this.hide = this.hide ?? false;
  }
}

export function getProductReleaseIdentifier(productRelease: IProductRelease): number | undefined {
  return productRelease.id;
}
