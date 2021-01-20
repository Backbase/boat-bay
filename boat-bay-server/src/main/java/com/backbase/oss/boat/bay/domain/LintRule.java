package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;

/**
 * A LintRule.
 */
@Entity
@Table(name = "lint_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LintRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "rule_set")
    private String ruleSet;

    @Column(name = "summary")
    private String summary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    @Column(name = "description")
    private String description;

    @Column(name = "external_url")
    private String externalUrl;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "lintRules", allowSetters = true)
    private Portal portal;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public LintRule ruleId(String ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTitle() {
        return title;
    }

    public LintRule title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public LintRule ruleSet(String ruleSet) {
        this.ruleSet = ruleSet;
        return this;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
    }

    public String getSummary() {
        return summary;
    }

    public LintRule summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Severity getSeverity() {
        return severity;
    }

    public LintRule severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public LintRule description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public LintRule externalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public LintRule enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Portal getPortal() {
        return portal;
    }

    public LintRule portal(Portal portal) {
        this.portal = portal;
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LintRule)) {
            return false;
        }
        return id != null && id.equals(((LintRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LintRule{" +
            "id=" + getId() +
            ", ruleId='" + getRuleId() + "'" +
            ", title='" + getTitle() + "'" +
            ", ruleSet='" + getRuleSet() + "'" +
            ", summary='" + getSummary() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", description='" + getDescription() + "'" +
            ", externalUrl='" + getExternalUrl() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
