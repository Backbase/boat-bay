import { Spec } from './spec';

export interface UiApiModule {
  title: string;
  version: string;
  portalPath: string;
  description: string;
  tags: string[];
  icon: string;
  specs: Spec[];
}
