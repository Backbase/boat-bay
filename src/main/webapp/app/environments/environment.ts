// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import { Provider } from '@angular/core';
import { IMAGES_DEFAULT_ASSET_PATH, NAVIGATION_FILE_PATH } from '../tokens';

export const environment = {
  production: false,
  lengthOfEllipsis: 140,
  providers: [
    {
      provide: IMAGES_DEFAULT_ASSET_PATH,
      useValue: '/content/icons/',
    },
    {
      provide: NAVIGATION_FILE_PATH,
      useValue: '/content/navigation-with-products.json',
    },
  ] as Provider[],
  lengthOfDescription: 140,
  lengthOfTitle: 35,
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
