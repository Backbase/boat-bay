/*
Boat Generator configuration:
    useBeanValidation: false
    useOptional: 
    addServletRequest: false
    useLombokAnnotations: false
    openApiNullable: true
    useSetForUniqueItems: false
    useWithModifiers: false
*/
package com.backbase.oss.boat.bay.service.api;

import com.backbase.oss.boat.bay.service.model.BoatCapability;
import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.BoatLintRule;
import com.backbase.oss.boat.bay.service.model.BoatPortal;
import com.backbase.oss.boat.bay.service.model.BoatPortalDashboard;
import com.backbase.oss.boat.bay.service.model.BoatProduct;
import com.backbase.oss.boat.bay.service.model.BoatProductRelease;
import com.backbase.oss.boat.bay.service.model.BoatService;
import com.backbase.oss.boat.bay.service.model.BoatSpec;
import com.backbase.oss.boat.bay.service.model.BoatTag;
import com.backbase.oss.boat.bay.service.model.UploadRequestBody;
import java.util.Set;
import java.util.LinkedHashSet;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")
@Api(value = "api", description = "the api API")
public interface ApiBoatBay {

    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version} : download spec
     *
     * @param portalKey portal idenifier (required)
     * @param productKey product identifier (required)
     * @param capabilityKey capability identifier (required)
     * @param serviceKey service identifier (required)
     * @param specKey spec identifier (required)
     * @param version version of spec (required)
     * @return spec with open api (status code 200)
     */
    @ApiOperation(value = "download spec", nickname = "downloadSpec", notes = "", response = BoatSpec.class, tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "spec with open api", response = BoatSpec.class) })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<BoatSpec> downloadSpec(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "product identifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "capability identifier",required=true)
            @PathVariable("capabilityKey")
            String capabilityKey
,
                    @ApiParam(value = "service identifier",required=true)
            @PathVariable("serviceKey")
            String serviceKey
,
                    @ApiParam(value = "spec identifier",required=true)
            @PathVariable("specKey")
            String specKey
,
                    @ApiParam(value = "version of spec",required=true)
            @PathVariable("version")
            String version

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/diff-report : get lint report for spec
     *
     * @param portalKey portal idenifier (required)
     * @param productKey product identifier (required)
     * @param spec1Id identifies first comparitable api (optional)
     * @param spec2Id identifies second comparitable api (optional)
     * @return changed openApi (status code 200)
     */
    @ApiOperation(value = "get lint report for spec", nickname = "getApiChangesForSpec", notes = "", response = org.openapitools.openapidiff.core.model.ChangedOpenApi.class, tags={ "lint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "changed openApi", response = org.openapitools.openapidiff.core.model.ChangedOpenApi.class) })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/diff-report",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<org.openapitools.openapidiff.core.model.ChangedOpenApi> getApiChangesForSpec(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "product identifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "identifies first comparitable api") 
            @RequestParam(value = "spec1Id", required = false)
            
            String spec1Id
,
                    @ApiParam(value = "identifies second comparitable api") 
            @RequestParam(value = "spec2Id", required = false)
            
            String spec2Id

    );


    /**
     * GET /api/boat/dashboard : get list of boat portal dashboards
     *
     * @return list of boat portal dashboards (status code 200)
     */
    @ApiOperation(value = "get list of boat portal dashboards", nickname = "getDashboard", notes = "", response = BoatPortalDashboard.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of boat portal dashboards", response = BoatPortalDashboard.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/dashboard",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatPortalDashboard>> getDashboard(

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/specs/{specId}/lint-report : get Lint Report for spec
     *
     * @param specId spec idenifier (required)
     * @param productKey product idenifier (required)
     * @param portalKey portal idenifier (required)
     * @param refresh refresh idicator (optional)
     * @return lint report for spec (status code 200)
     */
    @ApiOperation(value = "get Lint Report for spec", nickname = "getLintReportForSpec", notes = "", response = BoatLintReport.class, tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "lint report for spec", response = BoatLintReport.class) })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/specs/{specId}/lint-report",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<BoatLintReport> getLintReportForSpec(
            @ApiParam(value = "spec idenifier",required=true)
            @PathVariable("specId")
            String specId
,
                    @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "refresh idicator") 
            @RequestParam(value = "refresh", required = false)
            
            Boolean refresh

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/diff-report.html : get lint report for spec as html
     *
     * @param portalKey portal idenifier (required)
     * @param productKey product identifier (required)
     * @param spec1Id identifies first comparitable api (optional)
     * @param spec2Id identifies second comparitable api (optional)
     * @return changed openApi as html (status code 200)
     */
    @ApiOperation(value = "get lint report for spec as html", nickname = "getLintReportForSpecAsHtml", notes = "", response = String.class, tags={ "lint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "changed openApi as html", response = String.class) })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/diff-report.html",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> getLintReportForSpecAsHtml(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "product identifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "identifies first comparitable api") 
            @RequestParam(value = "spec1Id", required = false)
            
            String spec1Id
,
                    @ApiParam(value = "identifies second comparitable api") 
            @RequestParam(value = "spec2Id", required = false)
            
            String spec2Id

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/capabilities : get Portal Products Capabalities
     *
     * @param productKey product idenifier (required)
     * @param portalKey portal idenifier (required)
     * @param pageable  (optional)
     * @return list of capabilities for product (status code 200)
     */
    @ApiOperation(value = "get Portal Products Capabalities", nickname = "getPortalCapabilities", notes = "", response = BoatCapability.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of capabilities for product", response = BoatCapability.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/capabilities",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatCapability>> getPortalCapabilities(
            @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "") 
            
            org.springframework.data.domain.Pageable pageable

    );


    /**
     * GET /api/boat/portals/{portalKey}/lint-rules : get list of lint rules
     *
     * @param portalKey portal idenifier (required)
     * @return list of lint rules for portal (status code 200)
     */
    @ApiOperation(value = "get list of lint rules", nickname = "getPortalLintRules", notes = "", response = BoatLintRule.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of lint rules for portal", response = BoatLintRule.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/lint-rules",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatLintRule>> getPortalLintRules(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products : get products for identified portal
     *
     * @param portalKey portal idenifier (required)
     * @return list of protals products (status code 200)
     */
    @ApiOperation(value = "get products for identified portal", nickname = "getPortalProducts", notes = "", response = BoatProduct.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of protals products", response = BoatProduct.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatProduct>> getPortalProducts(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/services : get Portal services for a given product
     *
     * @param productKey product idenifier (required)
     * @param portalKey portal idenifier (required)
     * @param pageable  (optional)
     * @return list of services for product (status code 200)
     */
    @ApiOperation(value = "get Portal services for a given product", nickname = "getPortalServices", notes = "", response = BoatService.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of services for product", response = BoatService.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/services",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatService>> getPortalServices(
            @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "") 
            
            org.springframework.data.domain.Pageable pageable

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/specs : get Portal Specs for given product
     *
     * @param productKey product idenifier (required)
     * @param portalKey portal idenifier (required)
     * @param pageable  (optional)
     * @param capabilityId capablility idenifier (optional)
     * @param productReleaseId product release idenifier (optional)
     * @param serviceId service idenifier (optional)
     * @param grade grade of spec (optional)
     * @param backwardsCompatible backwards compatible indicator (optional)
     * @param changed changed indicator (optional)
     * @return list of specs for product (status code 200)
     */
    @ApiOperation(value = "get Portal Specs for given product", nickname = "getPortalSpecs", notes = "", response = BoatSpec.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of specs for product", response = BoatSpec.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/specs",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatSpec>> getPortalSpecs(
            @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "") 
            
            org.springframework.data.domain.Pageable pageable
,
                    @ApiParam(value = "capablility idenifier") 
            @RequestParam(value = "capabilityId", required = false)
            
            String capabilityId
,
                    @ApiParam(value = "product release idenifier") 
            @RequestParam(value = "productReleaseId", required = false)
            
            String productReleaseId
,
                    @ApiParam(value = "service idenifier") 
            @RequestParam(value = "serviceId", required = false)
            
            String serviceId
,
                    @ApiParam(value = "grade of spec") 
            @RequestParam(value = "grade", required = false)
            
            String grade
,
                    @ApiParam(value = "backwards compatible indicator") 
            @RequestParam(value = "backwardsCompatible", required = false)
            
            Boolean backwardsCompatible
,
                    @ApiParam(value = "changed indicator") 
            @RequestParam(value = "changed", required = false)
            
            Boolean changed

    );


    /**
     * GET /api/boat/portals : get list of portals
     *
     * @return boat legacy portal (status code 200)
     */
    @ApiOperation(value = "get list of portals", nickname = "getPortals", notes = "", response = BoatPortal.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "boat legacy portal", response = BoatPortal.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatPortal>> getPortals(

    );


    /**
     * GET /api/boat/dashboard/{projectKey}/{productKey} : get product for dashboard project
     *
     * @param projectKey project idenifier (required)
     * @param productKey product idenifier (required)
     * @return product for dashboard (status code 200)
     */
    @ApiOperation(value = "get product for dashboard project", nickname = "getProductDashboard", notes = "", response = BoatProduct.class, tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "product for dashboard", response = BoatProduct.class) })
    @RequestMapping(value = "/api/boat/dashboard/{projectKey}/{productKey}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<BoatProduct> getProductDashboard(
            @ApiParam(value = "project idenifier",required=true)
            @PathVariable("projectKey")
            String projectKey
,
                    @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/releases/{releaseKey}/specs : get specs for product release
     *
     * @param releaseKey key for product release (required)
     * @param productKey id of product (required)
     * @param portalKey portal idenifier (required)
     * @return list of specs for product release (status code 200)
     */
    @ApiOperation(value = "get specs for product release", nickname = "getProductReleaseSpecs", notes = "", response = BoatSpec.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of specs for product release", response = BoatSpec.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/releases/{releaseKey}/specs",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatSpec>> getProductReleaseSpecs(
            @ApiParam(value = "key for product release",required=true)
            @PathVariable("releaseKey")
            String releaseKey
,
                    @ApiParam(value = "id of product",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/releases : get product releases
     *
     * @param productKey id of product (required)
     * @param portalKey portal idenifier (required)
     * @return list of product releases for product (status code 200)
     */
    @ApiOperation(value = "get product releases", nickname = "getProductReleases", notes = "", response = BoatProductRelease.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of product releases for product", response = BoatProductRelease.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/releases",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatProductRelease>> getProductReleases(
            @ApiParam(value = "id of product",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/tags : get tags for product
     *
     * @param portalKey portal idenifier (required)
     * @param productKey product idenifier (required)
     * @return list of product tags (status code 200)
     */
    @ApiOperation(value = "get tags for product", nickname = "getProductTags", notes = "", response = BoatTag.class, responseContainer = "List", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of product tags", response = BoatTag.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/tags",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<BoatTag>> getProductTags(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "product idenifier",required=true)
            @PathVariable("productKey")
            String productKey

    );


    /**
     * GET /api/boat/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}/download : get spec as openapi
     *
     * @param portalKey portal idenifier (required)
     * @param productKey product identifier (required)
     * @param capabilityKey capability identifier (required)
     * @param serviceKey service identifier (required)
     * @param specKey spec identifier (required)
     * @param version version of spec (required)
     * @return openApi of spec (status code 200)
     */
    @ApiOperation(value = "get spec as openapi", nickname = "getSpecAsOpenAPI", notes = "", response = org.springframework.core.io.Resource.class, tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "openApi of spec", response = org.springframework.core.io.Resource.class) })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/products/{productKey}/capabilities/{capabilityKey}/services/{serviceKey}/specs/{specKey}/{version}/download",
        produces = { "application/vnd.oai.openapi" }, 
        method = RequestMethod.GET)
    ResponseEntity<org.springframework.core.io.Resource> getSpecAsOpenAPI(
            @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = "product identifier",required=true)
            @PathVariable("productKey")
            String productKey
,
                    @ApiParam(value = "capability identifier",required=true)
            @PathVariable("capabilityKey")
            String capabilityKey
,
                    @ApiParam(value = "service identifier",required=true)
            @PathVariable("serviceKey")
            String serviceKey
,
                    @ApiParam(value = "spec identifier",required=true)
            @PathVariable("specKey")
            String specKey
,
                    @ApiParam(value = "version of spec",required=true)
            @PathVariable("version")
            String version

    );


    /**
     * POST /api/boat/portals/{portalKey}/lint-rules/{lintRuleId} : update lint rule for portal
     *
     * @param lintRuleId id of portals lint rule (required)
     * @param portalKey portal idenifier (required)
     * @param boatLintRule  (optional)
     * @return updated successfuly (status code 200)
     */
    @ApiOperation(value = "update lint rule for portal", nickname = "updatePortalLintRule", notes = "", tags={ "dashboard", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "updated successfuly") })
    @RequestMapping(value = "/api/boat/portals/{portalKey}/lint-rules/{lintRuleId}",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> updatePortalLintRule(
            @ApiParam(value = "id of portals lint rule",required=true)
            @PathVariable("lintRuleId")
            String lintRuleId
,
                    @ApiParam(value = "portal idenifier",required=true)
            @PathVariable("portalKey")
            String portalKey
,
                    @ApiParam(value = ""  )

            @RequestBody(required = false) BoatLintRule boatLintRule

    );


    /**
     * PUT /api/boat/boat-maven-plugin/{sourceId}/upload : upload and lint specs
     *
     * @param sourceId source idenifier (required)
     * @param uploadRequestBody  (required)
     * @return list of lint reports for specs (status code 200)
     */
    @ApiOperation(value = "upload and lint specs", nickname = "uploadSpec", notes = "", response = BoatLintReport.class, responseContainer = "List", tags={ "upload-plugin", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "list of lint reports for specs", response = BoatLintReport.class, responseContainer = "List") })
    @RequestMapping(value = "/api/boat/boat-maven-plugin/{sourceId}/upload",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<List<BoatLintReport>> uploadSpec(
            @ApiParam(value = "source idenifier",required=true)
            @PathVariable("sourceId")
            String sourceId
,
                    @ApiParam(value = "" ,required=true )

            @RequestBody UploadRequestBody uploadRequestBody

    );

}
