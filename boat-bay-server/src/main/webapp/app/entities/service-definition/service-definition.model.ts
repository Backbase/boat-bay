import * as dayjs from 'dayjs';
import { ISpec } from 'app/entities/spec/spec.model';
import { ICapability } from 'app/entities/capability/capability.model';

export interface IServiceDefinition {
  id?: number;
  key?: string;
  name?: string;
  order?: number | null;
  subTitle?: string | null;
  description?: string | null;
  icon?: string | null;
  color?: string | null;
  createdOn?: dayjs.Dayjs | null;
  createdBy?: string | null;
  hide?: boolean | null;
  specs?: ISpec[] | null;
  capability?: ICapability;
}

export class ServiceDefinition implements IServiceDefinition {
  constructor(
    public id?: number,
    public key?: string,
    public name?: string,
    public order?: number | null,
    public subTitle?: string | null,
    public description?: string | null,
    public icon?: string | null,
    public color?: string | null,
    public createdOn?: dayjs.Dayjs | null,
    public createdBy?: string | null,
    public hide?: boolean | null,
    public specs?: ISpec[] | null,
    public capability?: ICapability
  ) {
    this.hide = this.hide ?? false;
  }
}

export function getServiceDefinitionIdentifier(serviceDefinition: IServiceDefinition): number | undefined {
  return serviceDefinition.id;
}
