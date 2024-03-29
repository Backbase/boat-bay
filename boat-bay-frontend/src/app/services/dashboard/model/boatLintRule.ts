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

import { Severity } from './severity';


export interface BoatLintRule { 
    ["id"]: number;
    ["ruleId"]: string;
    ["enabled"]: boolean;
    ["title"]: string;
    ["ruleSet"]: string;
    ["severity"]: Severity;
    ["url"]: string;
}

