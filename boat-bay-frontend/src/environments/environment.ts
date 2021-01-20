// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { Provider } from "@angular/core";
import { IMAGES_DEFAULT_ASSET_PATH, JIRA_BASE_URL, NAVIGATION_FILE_PATH } from "../app/app.constants";

export const environment = {
  production: false,
  providers: [
    {
      provide: IMAGES_DEFAULT_ASSET_PATH,
      useValue: '/assets/icons/',
    },
    {
      provide: NAVIGATION_FILE_PATH,
      useValue: '/assets/navigation-with-products.json',
    },
    {
      provide: JIRA_BASE_URL,
      useValue: 'https://backbase.atlassian.net/jira'
    },
  ] as Provider[],

};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
