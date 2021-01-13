import { ISpec } from 'app/shared/model/spec.model';

export interface ITag {
  id?: number;
  name?: string;
  description?: any;
  hide?: boolean;
  specs?: ISpec[];
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public description?: any, public hide?: boolean, public specs?: ISpec[]) {
    this.hide = this.hide || false;
  }
}
