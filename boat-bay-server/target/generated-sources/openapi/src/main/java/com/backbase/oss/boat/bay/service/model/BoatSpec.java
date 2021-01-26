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
import com.backbase.oss.boat.bay.service.model.BoatCapability;
import com.backbase.oss.boat.bay.service.model.BoatService;
import com.backbase.oss.boat.bay.service.model.BoatStatistics;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatSpec
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatSpec 
 {
    @JsonProperty("id")
    private BigDecimal id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("version")
    private String version;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("createdOn")
    private java.time.LocalDateTime createdOn;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("statistics")
    private BoatStatistics statistics;

    @JsonProperty("backwardsCompatible")
    private Boolean backwardsCompatible;

    @JsonProperty("changed")
    private Boolean changed;

    @JsonProperty("capability")
    private BoatCapability capability;

    @JsonProperty("serviceDefinition")
    private BoatService serviceDefinition;

    @JsonProperty("openApi")
    private String openApi;


    public BoatSpec id(BigDecimal id) {
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


    public BoatSpec key(String key) {
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


    public BoatSpec name(String name) {
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


    public BoatSpec title(String title) {
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


    public BoatSpec description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     * @return description
     */
    @ApiModelProperty(value = "")
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public BoatSpec icon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Get icon
     * @return icon
     */
    @ApiModelProperty(value = "")
    
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public BoatSpec version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     * @return version
     */
    @ApiModelProperty(value = "")
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public BoatSpec grade(String grade) {
        this.grade = grade;
        return this;
    }

    /**
     * Get grade
     * @return grade
     */
    @ApiModelProperty(value = "")
    
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public BoatSpec createdOn(java.time.LocalDateTime createdOn) {
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


    public BoatSpec createdBy(String createdBy) {
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


    public BoatSpec statistics(BoatStatistics statistics) {
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


    public BoatSpec backwardsCompatible(Boolean backwardsCompatible) {
        this.backwardsCompatible = backwardsCompatible;
        return this;
    }

    /**
     * Get backwardsCompatible
     * @return backwardsCompatible
     */
    @ApiModelProperty(value = "")
    
    public Boolean getBackwardsCompatible() {
        return backwardsCompatible;
    }

    public void setBackwardsCompatible(Boolean backwardsCompatible) {
        this.backwardsCompatible = backwardsCompatible;
    }


    public BoatSpec changed(Boolean changed) {
        this.changed = changed;
        return this;
    }

    /**
     * Get changed
     * @return changed
     */
    @ApiModelProperty(value = "")
    
    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }


    public BoatSpec capability(BoatCapability capability) {
        this.capability = capability;
        return this;
    }

    /**
     * Get capability
     * @return capability
     */
    @ApiModelProperty(value = "")
    
    public BoatCapability getCapability() {
        return capability;
    }

    public void setCapability(BoatCapability capability) {
        this.capability = capability;
    }


    public BoatSpec serviceDefinition(BoatService serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
        return this;
    }

    /**
     * Get serviceDefinition
     * @return serviceDefinition
     */
    @ApiModelProperty(value = "")
    
    public BoatService getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(BoatService serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }


    public BoatSpec openApi(String openApi) {
        this.openApi = openApi;
        return this;
    }

    /**
     * Get openApi
     * @return openApi
     */
    @ApiModelProperty(value = "")
    
    public String getOpenApi() {
        return openApi;
    }

    public void setOpenApi(String openApi) {
        this.openApi = openApi;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatSpec boatSpec = (BoatSpec) o;
        return Objects.equals(this.id, boatSpec.id) &&
                Objects.equals(this.key, boatSpec.key) &&
                Objects.equals(this.name, boatSpec.name) &&
                Objects.equals(this.title, boatSpec.title) &&
                Objects.equals(this.description, boatSpec.description) &&
                Objects.equals(this.icon, boatSpec.icon) &&
                Objects.equals(this.version, boatSpec.version) &&
                Objects.equals(this.grade, boatSpec.grade) &&
                Objects.equals(this.createdOn, boatSpec.createdOn) &&
                Objects.equals(this.createdBy, boatSpec.createdBy) &&
                Objects.equals(this.statistics, boatSpec.statistics) &&
                Objects.equals(this.backwardsCompatible, boatSpec.backwardsCompatible) &&
                Objects.equals(this.changed, boatSpec.changed) &&
                Objects.equals(this.capability, boatSpec.capability) &&
                Objects.equals(this.serviceDefinition, boatSpec.serviceDefinition) &&
                Objects.equals(this.openApi, boatSpec.openApi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            key,
            name,
            title,
            description,
            icon,
            version,
            grade,
            createdOn,
            createdBy,
            statistics,
            backwardsCompatible,
            changed,
            capability,
            serviceDefinition,
            openApi
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatSpec {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        title: ").append(toIndentedString(title)).append("\n");
        sb.append("        description: ").append(toIndentedString(description)).append("\n");
        sb.append("        icon: ").append(toIndentedString(icon)).append("\n");
        sb.append("        version: ").append(toIndentedString(version)).append("\n");
        sb.append("        grade: ").append(toIndentedString(grade)).append("\n");
        sb.append("        createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("        createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("        statistics: ").append(toIndentedString(statistics)).append("\n");
        sb.append("        backwardsCompatible: ").append(toIndentedString(backwardsCompatible)).append("\n");
        sb.append("        changed: ").append(toIndentedString(changed)).append("\n");
        sb.append("        capability: ").append(toIndentedString(capability)).append("\n");
        sb.append("        serviceDefinition: ").append(toIndentedString(serviceDefinition)).append("\n");
        sb.append("        openApi: ").append(toIndentedString(openApi)).append("\n");
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

