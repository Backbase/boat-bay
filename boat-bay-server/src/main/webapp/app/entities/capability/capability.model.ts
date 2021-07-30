import * as dayjs from 'dayjs';
import { IServiceDefinition } from 'app/entities/service-definition/service-definition.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ICapability {
  id?: number;
  key?: string;
  name?: string;
  order?: number | null;
  subTitle?: string | null;
  content?: string | null;
  createdOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  hide?: boolean | null;
  serviceDefinitions?: IServiceDefinition[] | null;
  product?: IProduct;
}

export class Capability implements ICapability {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number | null,
    public subTitle?: string | null,
    public content?: string | null,
    public createdOn?: dayjs.Dayjs | null,
    public createdBy?: string | null,
    public hide?: boolean | null,
    public serviceDefinitions?: IServiceDefinition[] | null,
    public product?: IProduct
  ) {
    this.hide = this.hide ?? false;
  }
}

export function getCapabilityIdentifier(capability: ICapability): number | undefined {
  return capability.id;
}
