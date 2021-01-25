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
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatProductRelease
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class BoatProductRelease 
 {
    @JsonProperty("id")
    private BigDecimal id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private String version;

    @JsonProperty("releaseDate")
    private java.time.LocalDateTime releaseDate;


    public BoatProductRelease id(BigDecimal id) {
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


    public BoatProductRelease name(String name) {
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


    public BoatProductRelease version(String version) {
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


    public BoatProductRelease releaseDate(java.time.LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    /**
     * Get releaseDate
     * @return releaseDate
     */
    @ApiModelProperty(value = "")
    
    public java.time.LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(java.time.LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatProductRelease boatProductRelease = (BoatProductRelease) o;
        return Objects.equals(this.id, boatProductRelease.id) &&
                Objects.equals(this.name, boatProductRelease.name) &&
                Objects.equals(this.version, boatProductRelease.version) &&
                Objects.equals(this.releaseDate, boatProductRelease.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            version,
            releaseDate
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatProductRelease {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        version: ").append(toIndentedString(version)).append("\n");
        sb.append("        releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
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

