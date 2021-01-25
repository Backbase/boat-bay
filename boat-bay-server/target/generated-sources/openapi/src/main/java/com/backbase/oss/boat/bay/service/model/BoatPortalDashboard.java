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
package com.backbase.oss.boat.bay.service.model;

import java.util.Objects;
import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.BoatStatistics;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatPortalDashboard
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class BoatPortalDashboard 
 {
    @JsonProperty("portalId")
    private String portalId;

    @JsonProperty("portalKey")
    private String portalKey;

    @JsonProperty("portalName")
    private String portalName;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("productKey")
    private String productKey;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("numberOfServices")
    private Long numberOfServices;

    @JsonProperty("numberOfCapabilities")
    private Long numberOfCapabilities;

    @JsonProperty("productDescription")
    private String productDescription;

    @JsonProperty("lastLintReport")
    private BoatLintReport lastLintReport;

    @JsonProperty("statistics")
    private BoatStatistics statistics;


    public BoatPortalDashboard portalId(String portalId) {
        this.portalId = portalId;
        return this;
    }

    /**
     * Get portalId
     * @return portalId
     */
    @ApiModelProperty(value = "")
    
    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }


    public BoatPortalDashboard portalKey(String portalKey) {
        this.portalKey = portalKey;
        return this;
    }

    /**
     * Get portalKey
     * @return portalKey
     */
    @ApiModelProperty(value = "")
    
    public String getPortalKey() {
        return portalKey;
    }

    public void setPortalKey(String portalKey) {
        this.portalKey = portalKey;
    }


    public BoatPortalDashboard portalName(String portalName) {
        this.portalName = portalName;
        return this;
    }

    /**
     * Get portalName
     * @return portalName
     */
    @ApiModelProperty(value = "")
    
    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }


    public BoatPortalDashboard productId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Get productId
     * @return productId
     */
    @ApiModelProperty(value = "")
    
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public BoatPortalDashboard productKey(String productKey) {
        this.productKey = productKey;
        return this;
    }

    /**
     * Get productKey
     * @return productKey
     */
    @ApiModelProperty(value = "")
    
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }


    public BoatPortalDashboard productName(String productName) {
        this.productName = productName;
        return this;
    }

    /**
     * Get productName
     * @return productName
     */
    @ApiModelProperty(value = "")
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public BoatPortalDashboard numberOfServices(Long numberOfServices) {
        this.numberOfServices = numberOfServices;
        return this;
    }

    /**
     * Get numberOfServices
     * @return numberOfServices
     */
    @ApiModelProperty(value = "")
    
    public Long getNumberOfServices() {
        return numberOfServices;
    }

    public void setNumberOfServices(Long numberOfServices) {
        this.numberOfServices = numberOfServices;
    }


    public BoatPortalDashboard numberOfCapabilities(Long numberOfCapabilities) {
        this.numberOfCapabilities = numberOfCapabilities;
        return this;
    }

    /**
     * Get numberOfCapabilities
     * @return numberOfCapabilities
     */
    @ApiModelProperty(value = "")
    
    public Long getNumberOfCapabilities() {
        return numberOfCapabilities;
    }

    public void setNumberOfCapabilities(Long numberOfCapabilities) {
        this.numberOfCapabilities = numberOfCapabilities;
    }


    public BoatPortalDashboard productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    /**
     * Get productDescription
     * @return productDescription
     */
    @ApiModelProperty(value = "")
    
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public BoatPortalDashboard lastLintReport(BoatLintReport lastLintReport) {
        this.lastLintReport = lastLintReport;
        return this;
    }

    /**
     * Get lastLintReport
     * @return lastLintReport
     */
    @ApiModelProperty(value = "")
    
    public BoatLintReport getLastLintReport() {
        return lastLintReport;
    }

    public void setLastLintReport(BoatLintReport lastLintReport) {
        this.lastLintReport = lastLintReport;
    }


    public BoatPortalDashboard statistics(BoatStatistics statistics) {
        this.statistics = statistics;
        return this;
    }

    /**
     * Get statistics
     * @return statistics
     */
    @ApiModelProperty(value = "")
    
    public BoatStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(BoatStatistics statistics) {
        this.statistics = statistics;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatPortalDashboard boatPortalDashboard = (BoatPortalDashboard) o;
        return Objects.equals(this.portalId, boatPortalDashboard.portalId) &&
                Objects.equals(this.portalKey, boatPortalDashboard.portalKey) &&
                Objects.equals(this.portalName, boatPortalDashboard.portalName) &&
                Objects.equals(this.productId, boatPortalDashboard.productId) &&
                Objects.equals(this.productKey, boatPortalDashboard.productKey) &&
                Objects.equals(this.productName, boatPortalDashboard.productName) &&
                Objects.equals(this.numberOfServices, boatPortalDashboard.numberOfServices) &&
                Objects.equals(this.numberOfCapabilities, boatPortalDashboard.numberOfCapabilities) &&
                Objects.equals(this.productDescription, boatPortalDashboard.productDescription) &&
                Objects.equals(this.lastLintReport, boatPortalDashboard.lastLintReport) &&
                Objects.equals(this.statistics, boatPortalDashboard.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            portalId,
            portalKey,
            portalName,
            productId,
            productKey,
            productName,
            numberOfServices,
            numberOfCapabilities,
            productDescription,
            lastLintReport,
            statistics
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatPortalDashboard {\n");
        
        sb.append("        portalId: ").append(toIndentedString(portalId)).append("\n");
        sb.append("        portalKey: ").append(toIndentedString(portalKey)).append("\n");
        sb.append("        portalName: ").append(toIndentedString(portalName)).append("\n");
        sb.append("        productId: ").append(toIndentedString(productId)).append("\n");
        sb.append("        productKey: ").append(toIndentedString(productKey)).append("\n");
        sb.append("        productName: ").append(toIndentedString(productName)).append("\n");
        sb.append("        numberOfServices: ").append(toIndentedString(numberOfServices)).append("\n");
        sb.append("        numberOfCapabilities: ").append(toIndentedString(numberOfCapabilities)).append("\n");
        sb.append("        productDescription: ").append(toIndentedString(productDescription)).append("\n");
        sb.append("        lastLintReport: ").append(toIndentedString(lastLintReport)).append("\n");
        sb.append("        statistics: ").append(toIndentedString(statistics)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n        ");
    }
}

