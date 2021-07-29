import { IPortal } from 'app/entities/portal/portal.model';

export interface IDashboard {
  id?: number;
  name?: string;
  title?: string | null;
  subTitle?: string | null;
  navTitle?: string | null;
  content?: string | null;
  defaultPortal?: IPortal;
}

export class Dashboard implements IDashboard {
  constructor(
    public id?: number,
    public name?: string,
    public title?: string | null,
    public subTitle?: string | null,
    public navTitle?: string | null,
    public content?: string | null,
    public defaultPortal?: IPortal
  ) {}
}

export function getDashboardIdentifier(dashboard: IDashboard): number | undefined {
  return dashboard.id;
}
