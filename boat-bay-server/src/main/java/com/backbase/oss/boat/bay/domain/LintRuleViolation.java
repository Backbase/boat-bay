package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LintRuleViolation.
 */
@Entity
@Table(name = "lint_rule_violation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LintRuleViolation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @Column(name = "line_start")
    private Integer lineStart;

    @Column(name = "line_end")
    private Integer lineEnd;

    @Column(name = "json_pointer")
    private String jsonPointer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "portal" }, allowSetters = true)
    private LintRule lintRule;

    @ManyToOne
    @JsonIgnoreProperties(value = { "spec", "violations" }, allowSetters = true)
    private LintReport lintReport;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LintRuleViolation id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LintRuleViolation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public LintRuleViolation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public LintRuleViolation url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public LintRuleViolation severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Integer getLineStart() {
        return this.lineStart;
    }

    public LintRuleViolation lineStart(Integer lineStart) {
        this.lineStart = lineStart;
        return this;
    }

    public void setLineStart(Integer lineStart) {
        this.lineStart = lineStart;
    }

    public Integer getLineEnd() {
        return this.lineEnd;
    }

    public LintRuleViolation lineEnd(Integer lineEnd) {
        this.lineEnd = lineEnd;
        return this;
    }

    public void setLineEnd(Integer lineEnd) {
        this.lineEnd = lineEnd;
    }

    public String getJsonPointer() {
        return this.jsonPointer;
    }

    public LintRuleViolation jsonPointer(String jsonPointer) {
        this.jsonPointer = jsonPointer;
        return this;
    }

    public void setJsonPointer(String jsonPointer) {
        this.jsonPointer = jsonPointer;
    }

    public LintRule getLintRule() {
        return this.lintRule;
    }

    public LintRuleViolation lintRule(LintRule lintRule) {
        this.setLintRule(lintRule);
        return this;
    }

    public void setLintRule(LintRule lintRule) {
        this.lintRule = lintRule;
    }

    public LintReport getLintReport() {
        return this.lintReport;
    }

    public LintRuleViolation lintReport(LintReport lintReport) {
        this.setLintReport(lintReport);
        return this;
    }

    public void setLintReport(LintReport lintReport) {
        this.lintReport = lintReport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LintRuleViolation)) {
            return false;
        }
        return id != null && id.equals(((LintRuleViolation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LintRuleViolation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", lineStart=" + getLineStart() +
            ", lineEnd=" + getLineEnd() +
            ", jsonPointer='" + getJsonPointer() + "'" +
            "}";
    }
}
