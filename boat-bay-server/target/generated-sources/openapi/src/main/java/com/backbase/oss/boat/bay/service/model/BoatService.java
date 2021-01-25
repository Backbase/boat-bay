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
import com.backbase.oss.boat.bay.service.model.BoatStatistics;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatService
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class BoatService 
 {
    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("color")
    private String color;

    @JsonProperty("createdOn")
    private java.time.LocalDateTime createdOn;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("statistics")
    private BoatStatistics statistics;


    public BoatService key(String key) {
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


    public BoatService name(String name) {
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


    public BoatService description(String description) {
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


    public BoatService icon(String icon) {
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


    public BoatService color(String color) {
        this.color = color;
        return this;
    }

    /**
     * Get color
     * @return color
     */
    @ApiModelProperty(value = "")
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public BoatService createdOn(java.time.LocalDateTime createdOn) {
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


    public BoatService createdBy(String createdBy) {
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


    public BoatService statistics(BoatStatistics statistics) {
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
        BoatService boatService = (BoatService) o;
        return Objects.equals(this.key, boatService.key) &&
                Objects.equals(this.name, boatService.name) &&
                Objects.equals(this.description, boatService.description) &&
                Objects.equals(this.icon, boatService.icon) &&
                Objects.equals(this.color, boatService.color) &&
                Objects.equals(this.createdOn, boatService.createdOn) &&
                Objects.equals(this.createdBy, boatService.createdBy) &&
                Objects.equals(this.statistics, boatService.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            key,
            name,
            description,
            icon,
            color,
            createdOn,
            createdBy,
            statistics
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatService {\n");
        
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        description: ").append(toIndentedString(description)).append("\n");
        sb.append("        icon: ").append(toIndentedString(icon)).append("\n");
        sb.append("        color: ").append(toIndentedString(color)).append("\n");
        sb.append("        createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("        createdBy: ").append(toIndentedString(createdBy)).append("\n");
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

