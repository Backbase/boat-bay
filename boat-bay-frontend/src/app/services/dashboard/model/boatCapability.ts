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

import { BoatLintReport } from './boatLintReport';
import { BoatService } from './boatService';
import { BoatStatistics } from './boatStatistics';


export interface BoatCapability { 
    ["id"]?: number;
    ["key"]?: string;
    ["name"]?: string;
    ["content"]?: string;
    ["createdOn"]?: string;
    ["createdBy"]?: string;
    ["services"]?: Array<BoatService>;
    ["lastLintReport"]?: BoatLintReport;
    ["statistics"]?: BoatStatistics;
}

