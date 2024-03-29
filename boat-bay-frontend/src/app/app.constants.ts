
import { InjectionToken } from '@angular/core';

export const SERVER_API_URL = "/";
export const IMAGES_DEFAULT_ASSET_PATH = new InjectionToken<string>('images.default.asset.path');
export const NAVIGATION_FILE_PATH = new InjectionToken<string>('path.to.navigation.file');
export const JIRA_BASE_URL = new InjectionToken<String>("jira.base.url");

