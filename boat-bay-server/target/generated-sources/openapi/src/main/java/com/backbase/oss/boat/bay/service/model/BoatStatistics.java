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
 * BoatStatistics
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatStatistics 
 {
    @JsonProperty("updatedOn")
    private java.time.LocalDateTime updatedOn;

    @JsonProperty("mustViolationsCount")
    private Long mustViolationsCount;

    @JsonProperty("shouldViolationsCount")
    private Long shouldViolationsCount;

    @JsonProperty("mayViolationsCount")
    private Long mayViolationsCount;

    @JsonProperty("hintViolationsCount")
    private Long hintViolationsCount;


    public BoatStatistics updatedOn(java.time.LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    /**
     * Get updatedOn
     * @return updatedOn
     */
    @ApiModelProperty(value = "")
    
    public java.time.LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(java.time.LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }


    public BoatStatistics mustViolationsCount(Long mustViolationsCount) {
        this.mustViolationsCount = mustViolationsCount;
        return this;
    }

    /**
     * Get mustViolationsCount
     * @return mustViolationsCount
     */
    @ApiModelProperty(value = "")
    
    public Long getMustViolationsCount() {
        return mustViolationsCount;
    }

    public void setMustViolationsCount(Long mustViolationsCount) {
        this.mustViolationsCount = mustViolationsCount;
    }


    public BoatStatistics shouldViolationsCount(Long shouldViolationsCount) {
        this.shouldViolationsCount = shouldViolationsCount;
        return this;
    }

    /**
     * Get shouldViolationsCount
     * @return shouldViolationsCount
     */
    @ApiModelProperty(value = "")
    
    public Long getShouldViolationsCount() {
        return shouldViolationsCount;
    }

    public void setShouldViolationsCount(Long shouldViolationsCount) {
        this.shouldViolationsCount = shouldViolationsCount;
    }


    public BoatStatistics mayViolationsCount(Long mayViolationsCount) {
        this.mayViolationsCount = mayViolationsCount;
        return this;
    }

    /**
     * Get mayViolationsCount
     * @return mayViolationsCount
     */
    @ApiModelProperty(value = "")
    
    public Long getMayViolationsCount() {
        return mayViolationsCount;
    }

    public void setMayViolationsCount(Long mayViolationsCount) {
        this.mayViolationsCount = mayViolationsCount;
    }


    public BoatStatistics hintViolationsCount(Long hintViolationsCount) {
        this.hintViolationsCount = hintViolationsCount;
        return this;
    }

    /**
     * Get hintViolationsCount
     * @return hintViolationsCount
     */
    @ApiModelProperty(value = "")
    
    public Long getHintViolationsCount() {
        return hintViolationsCount;
    }

    public void setHintViolationsCount(Long hintViolationsCount) {
        this.hintViolationsCount = hintViolationsCount;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatStatistics boatStatistics = (BoatStatistics) o;
        return Objects.equals(this.updatedOn, boatStatistics.updatedOn) &&
                Objects.equals(this.mustViolationsCount, boatStatistics.mustViolationsCount) &&
                Objects.equals(this.shouldViolationsCount, boatStatistics.shouldViolationsCount) &&
                Objects.equals(this.mayViolationsCount, boatStatistics.mayViolationsCount) &&
                Objects.equals(this.hintViolationsCount, boatStatistics.hintViolationsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            updatedOn,
            mustViolationsCount,
            shouldViolationsCount,
            mayViolationsCount,
            hintViolationsCount
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatStatistics {\n");
        
        sb.append("        updatedOn: ").append(toIndentedString(updatedOn)).append("\n");
        sb.append("        mustViolationsCount: ").append(toIndentedString(mustViolationsCount)).append("\n");
        sb.append("        shouldViolationsCount: ").append(toIndentedString(shouldViolationsCount)).append("\n");
        sb.append("        mayViolationsCount: ").append(toIndentedString(mayViolationsCount)).append("\n");
        sb.append("        hintViolationsCount: ").append(toIndentedString(hintViolationsCount)).append("\n");
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

