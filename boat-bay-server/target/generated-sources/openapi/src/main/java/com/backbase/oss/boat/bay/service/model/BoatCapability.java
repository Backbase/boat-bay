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
 * BoatCapability
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatCapability 
 {
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


    public BoatCapability id(BigDecimal id) {
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


    public BoatCapability key(String key) {
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


    public BoatCapability name(String name) {
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


    public BoatCapability content(String content) {
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


    public BoatCapability createdOn(java.time.LocalDateTime createdOn) {
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


    public BoatCapability createdBy(String createdBy) {
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


    public BoatCapability lastLintReport(BoatLintReport lastLintReport) {
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


    public BoatCapability statistics(BoatStatistics statistics) {
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
        BoatCapability boatCapability = (BoatCapability) o;
        return Objects.equals(this.id, boatCapability.id) &&
                Objects.equals(this.key, boatCapability.key) &&
                Objects.equals(this.name, boatCapability.name) &&
                Objects.equals(this.content, boatCapability.content) &&
                Objects.equals(this.createdOn, boatCapability.createdOn) &&
                Objects.equals(this.createdBy, boatCapability.createdBy) &&
                Objects.equals(this.lastLintReport, boatCapability.lastLintReport) &&
                Objects.equals(this.statistics, boatCapability.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            key,
            name,
            content,
            createdOn,
            createdBy,
            lastLintReport,
            statistics
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatCapability {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        content: ").append(toIndentedString(content)).append("\n");
        sb.append("        createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("        createdBy: ").append(toIndentedString(createdBy)).append("\n");
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

