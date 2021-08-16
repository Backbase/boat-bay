import { InjectionToken } from '@angular/core';

export const BOAT_DASHBOARD_BASE_PATH = new InjectionToken<string>('BOAT_DASHBOARD_BASE_PATH');
export const COLLECTION_FORMATS = {
    'csv': ',',
    'tsv': '   ',
    'ssv': ' ',
    'pipes': '|'
}
