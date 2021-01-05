import { Moment } from 'moment';
import { IServiceDefinition } from 'app/shared/model/service-definition.model';
import { IProduct } from 'app/shared/model/product.model';

export interface ICapability {
  id?: number;
  key?: string;
  name?: string;
  order?: number;
  title?: string;
  subTitle?: string;
  navTitle?: string;
  content?: any;
  createdOn?: Moment;
  createdBy?: string;
  hide?: boolean;
  serviceDefinitions?: IServiceDefinition[];
  product?: IProduct;
}

export class Capability implements ICapability {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number,
    public title?: string,
    public subTitle?: string,
    public navTitle?: string,
    public content?: any,
    public createdOn?: Moment,
    public createdBy?: string,
    public hide?: boolean,
    public serviceDefinitions?: IServiceDefinition[],
    public product?: IProduct
  ) {
    this.hide = this.hide || false;
  }
}
