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
import com.backbase.oss.boat.bay.client.model.Severity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.net.URI;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * BoatLintRule
 */
@JsonPropertyOrder({
  BoatLintRule.JSON_PROPERTY_ID,
  BoatLintRule.JSON_PROPERTY_RULE_ID,
  BoatLintRule.JSON_PROPERTY_ENABLED,
  BoatLintRule.JSON_PROPERTY_TITLE,
  BoatLintRule.JSON_PROPERTY_RULE_SET,
  BoatLintRule.JSON_PROPERTY_SEVERITY,
  BoatLintRule.JSON_PROPERTY_URL
})
@javax.annotation.processing.Generated(value = "com.backbase.oss.codegen.java.BoatJavaCodeGen", date = "2021-01-25T07:58:49.054921Z[Europe/London]")
public class BoatLintRule {
  public static final String JSON_PROPERTY_ID = "id";
  private BigDecimal id;

  public static final String JSON_PROPERTY_RULE_ID = "ruleId";
  private String ruleId;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_TITLE = "title";
  private String title;

  public static final String JSON_PROPERTY_RULE_SET = "ruleSet";
  private String ruleSet;

  public static final String JSON_PROPERTY_SEVERITY = "severity";
  private Severity severity;

  public static final String JSON_PROPERTY_URL = "url";
  private URI url;


  public BoatLintRule id(BigDecimal id) {
    
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


  public BoatLintRule ruleId(String ruleId) {
    
    this.ruleId = ruleId;
    return this;
  }

   /**
   * Get ruleId
   * @return ruleId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_RULE_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRuleId() {
    return ruleId;
  }


  public void setRuleId(String ruleId) {
    this.ruleId = ruleId;
  }


  public BoatLintRule enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Get enabled
   * @return enabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEnabled() {
    return enabled;
  }


  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }


  public BoatLintRule title(String title) {
    
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_TITLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public BoatLintRule ruleSet(String ruleSet) {
    
    this.ruleSet = ruleSet;
    return this;
  }

   /**
   * Get ruleSet
   * @return ruleSet
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_RULE_SET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRuleSet() {
    return ruleSet;
  }


  public void setRuleSet(String ruleSet) {
    this.ruleSet = ruleSet;
  }


  public BoatLintRule severity(Severity severity) {
    
    this.severity = severity;
    return this;
  }

   /**
   * Get severity
   * @return severity
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_SEVERITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Severity getSeverity() {
    return severity;
  }


  public void setSeverity(Severity severity) {
    this.severity = severity;
  }


  public BoatLintRule url(URI url) {
    
    this.url = url;
    return this;
  }

   /**
   * Get url
   * @return url
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_URL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public URI getUrl() {
    return url;
  }


  public void setUrl(URI url) {
    this.url = url;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoatLintRule boatLintRule = (BoatLintRule) o;
    return Objects.equals(this.id, boatLintRule.id) &&
        Objects.equals(this.ruleId, boatLintRule.ruleId) &&
        Objects.equals(this.enabled, boatLintRule.enabled) &&
        Objects.equals(this.title, boatLintRule.title) &&
        Objects.equals(this.ruleSet, boatLintRule.ruleSet) &&
        Objects.equals(this.severity, boatLintRule.severity) &&
        Objects.equals(this.url, boatLintRule.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, ruleId, enabled, title, ruleSet, severity, url);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoatLintRule {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ruleId: ").append(toIndentedString(ruleId)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    ruleSet: ").append(toIndentedString(ruleSet)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

