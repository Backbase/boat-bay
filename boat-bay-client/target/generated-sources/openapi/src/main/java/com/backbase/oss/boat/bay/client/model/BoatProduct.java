/*
 * Boat Bay Client
 * Endpoints for the boat bay operations
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://github.com/Backbase/backbase-openapi-tools).
 * https://github.com/Backbase/backbase-openapi-tools
 * Do not edit the class manually.
 */


package com.backbase.oss.boat.bay.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.backbase.oss.boat.bay.client.model.BoatLintReport;
import com.backbase.oss.boat.bay.client.model.BoatStatistics;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * BoatProduct
 */
@JsonPropertyOrder({
  BoatProduct.JSON_PROPERTY_PORTAL_KEY,
  BoatProduct.JSON_PROPERTY_PORTAL_NAME,
  BoatProduct.JSON_PROPERTY_ID,
  BoatProduct.JSON_PROPERTY_KEY,
  BoatProduct.JSON_PROPERTY_NAME,
  BoatProduct.JSON_PROPERTY_CONTENT,
  BoatProduct.JSON_PROPERTY_CREATED_ON,
  BoatProduct.JSON_PROPERTY_CREATED_BY,
  BoatProduct.JSON_PROPERTY_LAST_LINT_REPORT,
  BoatProduct.JSON_PROPERTY_STATISTICS,
  BoatProduct.JSON_PROPERTY_JIRA_PROJECT_ID
})
@javax.annotation.processing.Generated(value = "com.backbase.oss.codegen.java.BoatJavaCodeGen", date = "2021-01-25T07:58:49.054921Z[Europe/London]")
public class BoatProduct {
  public static final String JSON_PROPERTY_PORTAL_KEY = "portalKey";
  private String portalKey;

  public static final String JSON_PROPERTY_PORTAL_NAME = "portalName";
  private String portalName;

  public static final String JSON_PROPERTY_ID = "id";
  private BigDecimal id;

  public static final String JSON_PROPERTY_KEY = "key";
  private String key;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_CONTENT = "content";
  private String content;

  public static final String JSON_PROPERTY_CREATED_ON = "createdOn";
  private java.time.LocalDateTime createdOn;

  public static final String JSON_PROPERTY_CREATED_BY = "createdBy";
  private String createdBy;

  public static final String JSON_PROPERTY_LAST_LINT_REPORT = "lastLintReport";
  private BoatLintReport lastLintReport;

  public static final String JSON_PROPERTY_STATISTICS = "statistics";
  private BoatStatistics statistics;

  public static final String JSON_PROPERTY_JIRA_PROJECT_ID = "jiraProjectId";
  private String jiraProjectId;


  public BoatProduct portalKey(String portalKey) {
    
    this.portalKey = portalKey;
    return this;
  }

   /**
   * Get portalKey
   * @return portalKey
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PORTAL_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPortalKey() {
    return portalKey;
  }


  public void setPortalKey(String portalKey) {
    this.portalKey = portalKey;
  }


  public BoatProduct portalName(String portalName) {
    
    this.portalName = portalName;
    return this;
  }

   /**
   * Get portalName
   * @return portalName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PORTAL_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPortalName() {
    return portalName;
  }


  public void setPortalName(String portalName) {
    this.portalName = portalName;
  }


  public BoatProduct id(BigDecimal id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BigDecimal getId() {
    return id;
  }


  public void setId(BigDecimal id) {
    this.id = id;
  }


  public BoatProduct key(String key) {
    
    this.key = key;
    return this;
  }

   /**
   * Get key
   * @return key
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getKey() {
    return key;
  }


  public void setKey(String key) {
    this.key = key;
  }


  public BoatProduct name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public BoatProduct content(String content) {
    
    this.content = content;
    return this;
  }

   /**
   * Get content
   * @return content
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getContent() {
    return content;
  }


  public void setContent(String content) {
    this.content = content;
  }


  public BoatProduct createdOn(java.time.LocalDateTime createdOn) {
    
    this.createdOn = createdOn;
    return this;
  }

   /**
   * Get createdOn
   * @return createdOn
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_CREATED_ON)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public java.time.LocalDateTime getCreatedOn() {
    return createdOn;
  }


  public void setCreatedOn(java.time.LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }


  public BoatProduct createdBy(String createdBy) {
    
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Get createdBy
   * @return createdBy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_CREATED_BY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCreatedBy() {
    return createdBy;
  }


  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public BoatProduct lastLintReport(BoatLintReport lastLintReport) {
    
    this.lastLintReport = lastLintReport;
    return this;
  }

   /**
   * Get lastLintReport
   * @return lastLintReport
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_LAST_LINT_REPORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BoatLintReport getLastLintReport() {
    return lastLintReport;
  }


  public void setLastLintReport(BoatLintReport lastLintReport) {
    this.lastLintReport = lastLintReport;
  }


  public BoatProduct statistics(BoatStatistics statistics) {
    
    this.statistics = statistics;
    return this;
  }

   /**
   * Get statistics
   * @return statistics
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_STATISTICS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BoatStatistics getStatistics() {
    return statistics;
  }


  public void setStatistics(BoatStatistics statistics) {
    this.statistics = statistics;
  }


  public BoatProduct jiraProjectId(String jiraProjectId) {
    
    this.jiraProjectId = jiraProjectId;
    return this;
  }

   /**
   * Get jiraProjectId
   * @return jiraProjectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_JIRA_PROJECT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getJiraProjectId() {
    return jiraProjectId;
  }


  public void setJiraProjectId(String jiraProjectId) {
    this.jiraProjectId = jiraProjectId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoatProduct boatProduct = (BoatProduct) o;
    return Objects.equals(this.portalKey, boatProduct.portalKey) &&
        Objects.equals(this.portalName, boatProduct.portalName) &&
        Objects.equals(this.id, boatProduct.id) &&
        Objects.equals(this.key, boatProduct.key) &&
        Objects.equals(this.name, boatProduct.name) &&
        Objects.equals(this.content, boatProduct.content) &&
        Objects.equals(this.createdOn, boatProduct.createdOn) &&
        Objects.equals(this.createdBy, boatProduct.createdBy) &&
        Objects.equals(this.lastLintReport, boatProduct.lastLintReport) &&
        Objects.equals(this.statistics, boatProduct.statistics) &&
        Objects.equals(this.jiraProjectId, boatProduct.jiraProjectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portalKey, portalName, id, key, name, content, createdOn, createdBy, lastLintReport, statistics, jiraProjectId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoatProduct {\n");
    sb.append("    portalKey: ").append(toIndentedString(portalKey)).append("\n");
    sb.append("    portalName: ").append(toIndentedString(portalName)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    lastLintReport: ").append(toIndentedString(lastLintReport)).append("\n");
    sb.append("    statistics: ").append(toIndentedString(statistics)).append("\n");
    sb.append("    jiraProjectId: ").append(toIndentedString(jiraProjectId)).append("\n");
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
    return o.toString().replace("\n", "\n    ");
  }

}

