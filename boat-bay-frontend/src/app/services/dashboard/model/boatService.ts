/**
 * Boat Bay Dashboard Server API
 * Endpoints for the boat bay operations
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://github.com/Backbase/backbase-openapi-tools).
 * https://github.com/Backbase/backbase-openapi-tools
 * Do not edit the class manually.
 */

import { BoatCapability } from './boatCapability';
import { BoatStatistics } from './boatStatistics';


export interface BoatService { 
    ["id"]: number;
    ["key"]: string;
    ["name"]: string;
    ["description"]?: string;
    ["icon"]?: string;
    ["color"]?: string;
    ["createdOn"]?: string;
    ["createdBy"]?: string;
    ["statistics"]?: BoatStatistics;
    ["capability"]: BoatCapability;
}

