export interface ISpecType {
  id?: number;
  name?: string;
  description?: string;
  matchSpEL?: string;
  icon?: string;
}

export class SpecType implements ISpecType {
  constructor(public id?: number, public name?: string, public description?: string, public matchSpEL?: string, public icon?: string) {}
}
