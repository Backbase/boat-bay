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
 * UploadSpec
 */
@javax.annotation.Generated(value = "com.backbase.oss.codegen.java.BoatSpringCodeGen", date = "2021-01-25T07:38:28.242919Z[Europe/London]")


public class UploadSpec 
 {
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("openApi")
    private String openApi;

    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;


    public UploadSpec fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * Get fileName
     * @return fileName
     */
    @ApiModelProperty(value = "")
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public UploadSpec openApi(String openApi) {
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


    public UploadSpec key(String key) {
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


    public UploadSpec name(String name) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadSpec uploadSpec = (UploadSpec) o;
        return Objects.equals(this.fileName, uploadSpec.fileName) &&
                Objects.equals(this.openApi, uploadSpec.openApi) &&
                Objects.equals(this.key, uploadSpec.key) &&
                Objects.equals(this.name, uploadSpec.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            fileName,
            openApi,
            key,
            name
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UploadSpec {\n");
        
        sb.append("        fileName: ").append(toIndentedString(fileName)).append("\n");
        sb.append("        openApi: ").append(toIndentedString(openApi)).append("\n");
        sb.append("        key: ").append(toIndentedString(key)).append("\n");
        sb.append("        name: ").append(toIndentedString(name)).append("\n");
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

