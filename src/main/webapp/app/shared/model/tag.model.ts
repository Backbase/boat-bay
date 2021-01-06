import { ISpec } from 'app/shared/model/spec.model';

export interface ITag {
  id?: number;
  name?: string;
  description?: string;
  specs?: ISpec[];
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public description?: string, public specs?: ISpec[]) {}
}
