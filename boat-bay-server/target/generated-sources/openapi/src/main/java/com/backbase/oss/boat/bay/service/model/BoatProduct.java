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
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatProduct
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatProduct 
 {
    @JsonProperty("portalKey")
    private String portalKey;

    @JsonProperty("portalName")
    private String portalName;

    @JsonProperty("id")
    private BigDecimal id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("content")
    private String content;

    @JsonProperty("createdOn")
    private java.time.LocalDateTime createdOn;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("lastLintReport")
    private BoatLintReport lastLintReport;

    @JsonProperty("statistics")
    private BoatStatistics statistics;

    @JsonProperty("jiraProjectId")
    private String jiraProjectId;


    public BoatProduct portalKey(String portalKey) {
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


    public BoatProduct portalName(String portalName) {
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


    public BoatProduct id(BigDecimal id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     * @return id
     */
    @ApiModelProperty(value = "")
    
    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }


    public BoatProduct key(String key) {
        this.key = key;
        return this;
    }

    /**
     * Get key
     * @return key
     */
    @ApiModelProperty(value = "")
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public BoatProduct name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     */
    @ApiModelProperty(value = "")
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BoatProduct content(String content) {
        this.content = content;
        return this;
    }

    /**
     * Get content
     * @return content
     */
    @ApiModelProperty(value = "")
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public BoatProduct createdOn(java.time.LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    /**
     * Get createdOn
     * @return createdOn
     */
    @ApiModelProperty(value = "")
    
    public java.time.LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(java.time.LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }


    public BoatProduct createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Get createdBy
     * @return createdBy
     */
    @ApiModelProperty(value = "")
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public BoatProduct lastLintReport(BoatLintReport lastLintReport) {
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


    public BoatProduct statistics(BoatStatistics statistics) {
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


    public BoatProduct jiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
        return this;
    }

    /**
     * Get jiraProjectId
     * @return jiraProjectId
     */
    @ApiModelProperty(value = "")
    
    public String getJiraProjectId() {
        return jiraProjectId;
    }

    public void setJiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatProduct boatProduct = (BoatProduct) o;
        return Objects.equals(this.portalKey, boatProduct.portalKey) &&
                Objects.equals(this.portalName, boatProduct.portalName) &&
                Objects.equals(this.id, boatProduct.id) &&
                Objects.equals(this.key, boatProduct.key) &&
                Objects.equals(this.name, boatProduct.name) &&
                Objects.equals(this.content, boatProduct.content) &&
                Objects.equals(this.createdOn, boatProduct.createdOn) &&
                Objects.equals(this.createdBy, boatProduct.createdBy) &&
                Objects.equals(this.lastLintReport, boatProduct.lastLintReport) &&
                Objects.equals(this.statistics, boatProduct.statistics) &&
                Objects.equals(this.jiraProjectId, boatProduct.jiraProjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            portalKey,
            portalName,
            id,
            key,
            name,
            content,
            createdOn,
            createdBy,
            lastLintReport,
            statistics,
            jiraProjectId
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatProduct {\n");
        
        sb.append("        portalKey: ").append(toIndentedString(portalKey)).append("\n");
        sb.append("        portalName: ").append(toIndentedString(portalName)).append("\n");
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        content: ").append(toIndentedString(content)).append("\n");
        sb.append("        createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("        createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("        lastLintReport: ").append(toIndentedString(lastLintReport)).append("\n");
        sb.append("        statistics: ").append(toIndentedString(statistics)).append("\n");
        sb.append("        jiraProjectId: ").append(toIndentedString(jiraProjectId)).append("\n");
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

