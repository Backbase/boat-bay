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

import { BoatViolation } from './boatViolation';
import { BoatSpec } from './boatSpec';


export interface BoatLintReport { 
    ["id"]: number;
    ["spec"]: BoatSpec;
    ["name"]: string;
    ["passed"]: boolean;
    ["lintedOn"]: string;
    ["openApi"]: string;
    ["version"]: string;
    ["grade"]: string;
    ["violations"]: Array<BoatViolation>;
}
