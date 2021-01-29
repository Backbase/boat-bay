import { IMAGES_DEFAULT_ASSET_PATH, JIRA_BASE_URL, NAVIGATION_FILE_PATH } from "../app/app.constants";
import { Provider } from "@angular/core";

export const environment = {
  production: true,
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
