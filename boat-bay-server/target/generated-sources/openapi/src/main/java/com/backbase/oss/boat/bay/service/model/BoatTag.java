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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatTag
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatTag 
 {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("hide")
    private Boolean hide;

    @JsonProperty("color")
    private String color;

    @JsonProperty("numberOfOccurrences")
    private Integer numberOfOccurrences;


    public BoatTag name(String name) {
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


    public BoatTag description(String description) {
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


    public BoatTag hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    /**
     * Get hide
     * @return hide
     */
    @ApiModelProperty(value = "")
    
    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }


    public BoatTag color(String color) {
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


    public BoatTag numberOfOccurrences(Integer numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
        return this;
    }

    /**
     * Get numberOfOccurrences
     * @return numberOfOccurrences
     */
    @ApiModelProperty(value = "")
    
    public Integer getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void setNumberOfOccurrences(Integer numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatTag boatTag = (BoatTag) o;
        return Objects.equals(this.name, boatTag.name) &&
                Objects.equals(this.description, boatTag.description) &&
                Objects.equals(this.hide, boatTag.hide) &&
                Objects.equals(this.color, boatTag.color) &&
                Objects.equals(this.numberOfOccurrences, boatTag.numberOfOccurrences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            name,
            description,
            hide,
            color,
            numberOfOccurrences
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatTag {\n");
        
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        description: ").append(toIndentedString(description)).append("\n");
        sb.append("        hide: ").append(toIndentedString(hide)).append("\n");
        sb.append("        color: ").append(toIndentedString(color)).append("\n");
        sb.append("        numberOfOccurrences: ").append(toIndentedString(numberOfOccurrences)).append("\n");
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

