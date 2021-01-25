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
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets Severity
 */
public enum Severity {
  
  MUST("MUST"),
  
  SHOULD("SHOULD"),
  
  MAY("MAY"),
  
  HINT("HINT");

  private String value;

  Severity(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Severity fromValue(String value) {
    for (Severity b : Severity.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

