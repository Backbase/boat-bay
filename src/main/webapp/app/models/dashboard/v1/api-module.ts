import { Spec } from './';

export interface ApiModule {
  title: string;
  versions: { [key: string]: string };
  description: string;
  tags: string[];
  id?: number;
  'x-icon'?: string;
  specs: { [key: string]: Spec };
}
