package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @Column(name = "description")
    private String description;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(mappedBy = "lintRule")
    @JsonIgnore
    private LintRuleViolation lintRuleViolation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LintRuleViolation getLintRuleViolation() {
        return lintRuleViolation;
    }

    public LintRule lintRuleViolation(LintRuleViolation lintRuleViolation) {
        this.lintRuleViolation = lintRuleViolation;
        return this;
    }

    public void setLintRuleViolation(LintRuleViolation lintRuleViolation) {
        this.lintRuleViolation = lintRuleViolation;
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
            ", title='" + getTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", description='" + getDescription() + "'" +
            ", externalUrl='" + getExternalUrl() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
