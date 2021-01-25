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
import com.backbase.oss.boat.bay.service.model.BoatLintRule;
import com.backbase.oss.boat.bay.service.model.Severity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatViolation
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class BoatViolation 
 {
    @JsonProperty("rule")
    private BoatLintRule rule;

    @JsonProperty("description")
    private String description;

    @JsonProperty("severity")
    private Severity severity;

    @JsonProperty("lines")
    private kotlin.ranges.IntRange lines;

    @JsonProperty("pointer")
    private String pointer;


    public BoatViolation rule(BoatLintRule rule) {
        this.rule = rule;
        return this;
    }

    /**
     * Get rule
     * @return rule
     */
    @ApiModelProperty(value = "")
    
    public BoatLintRule getRule() {
        return rule;
    }

    public void setRule(BoatLintRule rule) {
        this.rule = rule;
    }


    public BoatViolation description(String description) {
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


    public BoatViolation severity(Severity severity) {
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


    public BoatViolation lines(kotlin.ranges.IntRange lines) {
        this.lines = lines;
        return this;
    }

    /**
     * Get lines
     * @return lines
     */
    @ApiModelProperty(value = "")
    
    public kotlin.ranges.IntRange getLines() {
        return lines;
    }

    public void setLines(kotlin.ranges.IntRange lines) {
        this.lines = lines;
    }


    public BoatViolation pointer(String pointer) {
        this.pointer = pointer;
        return this;
    }

    /**
     * Get pointer
     * @return pointer
     */
    @ApiModelProperty(value = "")
    
    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatViolation boatViolation = (BoatViolation) o;
        return Objects.equals(this.rule, boatViolation.rule) &&
                Objects.equals(this.description, boatViolation.description) &&
                Objects.equals(this.severity, boatViolation.severity) &&
                Objects.equals(this.lines, boatViolation.lines) &&
                Objects.equals(this.pointer, boatViolation.pointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            rule,
            description,
            severity,
            lines,
            pointer
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatViolation {\n");
        
        sb.append("        rule: ").append(toIndentedString(rule)).append("\n");
        sb.append("        description: ").append(toIndentedString(description)).append("\n");
        sb.append("        severity: ").append(toIndentedString(severity)).append("\n");
        sb.append("        lines: ").append(toIndentedString(lines)).append("\n");
        sb.append("        pointer: ").append(toIndentedString(pointer)).append("\n");
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

