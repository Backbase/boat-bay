import { Capability } from './capability';
import { Product } from './product';

export interface ProductRelease {
  title: string;
  modules: { [key: string]: string };
}

export interface PortalView {
  capabilities: { [key: string]: Capability };
  releases: { [key: string]: ProductRelease };
  products: { [key: string]: Product };

  // key: String
  // name: String;
  // title: String;
}
