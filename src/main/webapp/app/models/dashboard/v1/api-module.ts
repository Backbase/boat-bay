import { Spec } from './';

export interface ApiModule {
  key: string;
  name: string;
  versions: { [key: string]: string };
  description: string;
  tags: string[];
  id?: number;
  'x-icon'?: string;
  specs: { [key: number]: Spec };
}
