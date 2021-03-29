import { IPortal } from 'app/shared/model/portal.model';

export interface IZallyConfig {
  id?: number;
  name?: string;
  contents?: any;
  portal?: IPortal;
}

export class ZallyConfig implements IZallyConfig {
  constructor(public id?: number, public name?: string, public contents?: any, public portal?: IPortal) {}
}
