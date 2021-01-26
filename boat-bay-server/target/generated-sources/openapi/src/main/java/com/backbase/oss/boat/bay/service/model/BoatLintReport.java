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
import com.backbase.oss.boat.bay.service.model.BoatSpec;
import com.backbase.oss.boat.bay.service.model.BoatViolation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * BoatLintReport
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-26T08:05:16.337980Z[Europe/London]")


public class BoatLintReport 
 {
    @JsonProperty("id")
    private BigDecimal id;

    @JsonProperty("spec")
    private BoatSpec spec;

    @JsonProperty("name")
    private String name;

    @JsonProperty("passed")
    private Boolean passed;

    @JsonProperty("lintedOn")
    private java.time.LocalDateTime lintedOn;

    @JsonProperty("openApi")
    private String openApi;

    @JsonProperty("version")
    private String version;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("violations")
    private List<BoatViolation> violations = null;

    @JsonProperty("hasViolations")
    private Boolean hasViolations;


    public BoatLintReport id(BigDecimal id) {
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


    public BoatLintReport spec(BoatSpec spec) {
        this.spec = spec;
        return this;
    }

    /**
     * Get spec
     * @return spec
     */
    @ApiModelProperty(value = "")
    
    public BoatSpec getSpec() {
        return spec;
    }

    public void setSpec(BoatSpec spec) {
        this.spec = spec;
    }


    public BoatLintReport name(String name) {
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


    public BoatLintReport passed(Boolean passed) {
        this.passed = passed;
        return this;
    }

    /**
     * Get passed
     * @return passed
     */
    @ApiModelProperty(value = "")
    
    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }


    public BoatLintReport lintedOn(java.time.LocalDateTime lintedOn) {
        this.lintedOn = lintedOn;
        return this;
    }

    /**
     * Get lintedOn
     * @return lintedOn
     */
    @ApiModelProperty(value = "")
    
    public java.time.LocalDateTime getLintedOn() {
        return lintedOn;
    }

    public void setLintedOn(java.time.LocalDateTime lintedOn) {
        this.lintedOn = lintedOn;
    }


    public BoatLintReport openApi(String openApi) {
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


    public BoatLintReport version(String version) {
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


    public BoatLintReport grade(String grade) {
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


    public BoatLintReport violations(List<BoatViolation> violations) {
        this.violations = violations;
        return this;
    }

    public BoatLintReport addViolationsItem(BoatViolation violationsItem) {
        if (this.violations == null) {
            this.violations = new ArrayList<>();
        }
        this.violations.add(violationsItem);
        return this;
    }

    /**
     * Get violations
     * @return violations
     */
    @ApiModelProperty(value = "")
    
    public List<BoatViolation> getViolations() {
        return violations;
    }

    public void setViolations(List<BoatViolation> violations) {
        this.violations = violations;
    }


    public BoatLintReport hasViolations(Boolean hasViolations) {
        this.hasViolations = hasViolations;
        return this;
    }

    /**
     * Get hasViolations
     * @return hasViolations
     */
    @ApiModelProperty(value = "")
    
    public Boolean getHasViolations() {
        return hasViolations;
    }

    public void setHasViolations(Boolean hasViolations) {
        this.hasViolations = hasViolations;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoatLintReport boatLintReport = (BoatLintReport) o;
        return Objects.equals(this.id, boatLintReport.id) &&
                Objects.equals(this.spec, boatLintReport.spec) &&
                Objects.equals(this.name, boatLintReport.name) &&
                Objects.equals(this.passed, boatLintReport.passed) &&
                Objects.equals(this.lintedOn, boatLintReport.lintedOn) &&
                Objects.equals(this.openApi, boatLintReport.openApi) &&
                Objects.equals(this.version, boatLintReport.version) &&
                Objects.equals(this.grade, boatLintReport.grade) &&
                Objects.equals(this.violations, boatLintReport.violations) &&
                Objects.equals(this.hasViolations, boatLintReport.hasViolations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            spec,
            name,
            passed,
            lintedOn,
            openApi,
            version,
            grade,
            violations,
            hasViolations
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BoatLintReport {\n");
        
        sb.append("        id: ").append(toIndentedString(id)).append("\n");
        sb.append("        spec: ").append(toIndentedString(spec)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
        sb.append("        passed: ").append(toIndentedString(passed)).append("\n");
        sb.append("        lintedOn: ").append(toIndentedString(lintedOn)).append("\n");
        sb.append("        openApi: ").append(toIndentedString(openApi)).append("\n");
        sb.append("        version: ").append(toIndentedString(version)).append("\n");
        sb.append("        grade: ").append(toIndentedString(grade)).append("\n");
        sb.append("        violations: ").append(toIndentedString(violations)).append("\n");
        sb.append("        hasViolations: ").append(toIndentedString(hasViolations)).append("\n");
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

