import { ISpec } from 'app/entities/spec/spec.model';

export interface ITag {
  id?: number;
  name?: string;
  description?: string | null;
  hide?: boolean | null;
  color?: string | null;
  specs?: ISpec[] | null;
}

export class Tag implements ITag {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public hide?: boolean | null,
    public color?: string | null,
    public specs?: ISpec[] | null
  ) {
    this.hide = this.hide ?? false;
  }
}

export function getTagIdentifier(tag: ITag): number | undefined {
  return tag.id;
}
