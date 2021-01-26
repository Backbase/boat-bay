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
import com.backbase.oss.boat.bay.service.model.Severity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.net.URI;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatLintRule
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatLintRule 
 {
    @JsonProperty("id")
    private BigDecimal id;

    @JsonProperty("ruleId")
    private String ruleId;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("title")
    private String title;

    @JsonProperty("ruleSet")
    private String ruleSet;

    @JsonProperty("severity")
    private Severity severity;

    @JsonProperty("url")
    private URI url;


    public BoatLintRule id(BigDecimal id) {
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


    public BoatLintRule ruleId(String ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    /**
     * Get ruleId
     * @return ruleId
     */
    @ApiModelProperty(value = "")
    
    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }


    public BoatLintRule enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Get enabled
     * @return enabled
     */
    @ApiModelProperty(value = "")
    
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public BoatLintRule title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get title
     * @return title
     */
    @ApiModelProperty(value = "")
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public BoatLintRule ruleSet(String ruleSet) {
        this.ruleSet = ruleSet;
        return this;
    }

    /**
     * Get ruleSet
     * @return ruleSet
     */
    @ApiModelProperty(value = "")
    
    public String getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
    }


    public BoatLintRule severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    /**
     * Get severity
     * @return severity
     */
    @ApiModelProperty(value = "")
    
    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }


    public BoatLintRule url(URI url) {
        this.url = url;
        return this;
    }

    /**
     * Get url
     * @return url
     */
    @ApiModelProperty(value = "")
    
    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatLintRule boatLintRule = (BoatLintRule) o;
        return Objects.equals(this.id, boatLintRule.id) &&
                Objects.equals(this.ruleId, boatLintRule.ruleId) &&
                Objects.equals(this.enabled, boatLintRule.enabled) &&
                Objects.equals(this.title, boatLintRule.title) &&
                Objects.equals(this.ruleSet, boatLintRule.ruleSet) &&
                Objects.equals(this.severity, boatLintRule.severity) &&
                Objects.equals(this.url, boatLintRule.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ruleId,
            enabled,
            title,
            ruleSet,
            severity,
            url
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatLintRule {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        ruleId: ").append(toIndentedString(ruleId)).append("\n");
        sb.append("        enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("        title: ").append(toIndentedString(title)).append("\n");
        sb.append("        ruleSet: ").append(toIndentedString(ruleSet)).append("\n");
        sb.append("        severity: ").append(toIndentedString(severity)).append("\n");
        sb.append("        url: ").append(toIndentedString(url)).append("\n");
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

