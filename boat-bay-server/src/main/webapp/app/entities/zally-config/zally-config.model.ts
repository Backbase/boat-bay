import { IPortal } from 'app/entities/portal/portal.model';

export interface IZallyConfig {
  id?: number;
  name?: string;
  contents?: string | null;
  portal?: IPortal | null;
}

export class ZallyConfig implements IZallyConfig {
  constructor(public id?: number, public name?: string, public contents?: string | null, public portal?: IPortal | null) {}
}

export function getZallyConfigIdentifier(zallyConfig: IZallyConfig): number | undefined {
  return zallyConfig.id;
}
