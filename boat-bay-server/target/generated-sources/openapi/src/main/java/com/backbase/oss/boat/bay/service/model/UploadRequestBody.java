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
import com.backbase.oss.boat.bay.service.model.UploadSpec;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

/**
 * UploadRequestBody
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class UploadRequestBody 
 {
    @JsonProperty("specs")
    private List<UploadSpec> specs = null;

    @JsonProperty("location")
    private String location;

    @JsonProperty("projectId")
    private String projectId;

    @JsonProperty("artifactId")
    private String artifactId;

    @JsonProperty("version")
    private String version;


    public UploadRequestBody specs(List<UploadSpec> specs) {
        this.specs = specs;
        return this;
    }

    public UploadRequestBody addSpecsItem(UploadSpec specsItem) {
        if (this.specs == null) {
            this.specs = new ArrayList<>();
        }
        this.specs.add(specsItem);
        return this;
    }

    /**
     * Get specs
     * @return specs
     */
    @ApiModelProperty(value = "")
    
    public List<UploadSpec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<UploadSpec> specs) {
        this.specs = specs;
    }


    public UploadRequestBody location(String location) {
        this.location = location;
        return this;
    }

    /**
     * Get location
     * @return location
     */
    @ApiModelProperty(value = "")
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public UploadRequestBody projectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * Get projectId
     * @return projectId
     */
    @ApiModelProperty(value = "")
    
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public UploadRequestBody artifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    /**
     * Get artifactId
     * @return artifactId
     */
    @ApiModelProperty(value = "")
    
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }


    public UploadRequestBody version(String version) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadRequestBody uploadRequestBody = (UploadRequestBody) o;
        return Objects.equals(this.specs, uploadRequestBody.specs) &&
                Objects.equals(this.location, uploadRequestBody.location) &&
                Objects.equals(this.projectId, uploadRequestBody.projectId) &&
                Objects.equals(this.artifactId, uploadRequestBody.artifactId) &&
                Objects.equals(this.version, uploadRequestBody.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            specs,
            location,
            projectId,
            artifactId,
            version
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UploadRequestBody {\n");
        
        sb.append("        specs: ").append(toIndentedString(specs)).append("\n");
        sb.append("        location: ").append(toIndentedString(location)).append("\n");
        sb.append("        projectId: ").append(toIndentedString(projectId)).append("\n");
        sb.append("        artifactId: ").append(toIndentedString(artifactId)).append("\n");
        sb.append("        version: ").append(toIndentedString(version)).append("\n");
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

