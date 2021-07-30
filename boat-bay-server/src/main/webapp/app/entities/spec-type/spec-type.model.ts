export interface ISpecType {
  id?: number;
  name?: string;
  description?: string | null;
  matchSpEL?: string | null;
  icon?: string;
}

export class SpecType implements ISpecType {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public matchSpEL?: string | null,
    public icon?: string
  ) {}
}

export function getSpecTypeIdentifier(specType: ISpecType): number | undefined {
  return specType.id;
}
