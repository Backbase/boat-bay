import { Spec } from 'app/models/spec';

export interface ApiModule {
  title: string;
  versions: { [key: string]: string };
  description: string;
  tags: string[];
  id?: number;
  'x-icon'?: string;
  specs: { [key: string]: Spec };
}
