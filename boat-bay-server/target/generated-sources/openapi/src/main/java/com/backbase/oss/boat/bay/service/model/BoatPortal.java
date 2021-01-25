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
 * BoatPortal
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class BoatPortal 
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


    public BoatPortal id(BigDecimal id) {
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


    public BoatPortal key(String key) {
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


    public BoatPortal name(String name) {
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


    public BoatPortal content(String content) {
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


    public BoatPortal createdOn(java.time.LocalDateTime createdOn) {
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


    public BoatPortal createdBy(String createdBy) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatPortal boatPortal = (BoatPortal) o;
        return Objects.equals(this.id, boatPortal.id) &&
                Objects.equals(this.key, boatPortal.key) &&
                Objects.equals(this.name, boatPortal.name) &&
                Objects.equals(this.content, boatPortal.content) &&
                Objects.equals(this.createdOn, boatPortal.createdOn) &&
                Objects.equals(this.createdBy, boatPortal.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            key,
            name,
            content,
            createdOn,
            createdBy
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatPortal {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        content: ").append(toIndentedString(content)).append("\n");
        sb.append("        createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("        createdBy: ").append(toIndentedString(createdBy)).append("\n");
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

