import { Spec } from './spec';

export interface UiApiModule {
  key: string;
  name: string;
  version: string;
  portalPath: string;
  description: string;
  tags: string[];
  icon: string;
  specs: Spec[];
}
