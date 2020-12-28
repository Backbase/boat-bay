package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;

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

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @Column(name = "line_start")
    private Integer lineStart;

    @Column(name = "lind_end")
    private Integer lindEnd;

    @Column(name = "column_start")
    private Integer columnStart;

    @Column(name = "column_end")
    private Integer columnEnd;

    @Column(name = "json_pointer")
    private String jsonPointer;

    @OneToOne
    @JoinColumn(unique = true)
    private LintRule lintRule;

    @OneToMany(mappedBy = "linkRuleViolation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LintReport> lintReports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LintRuleViolation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public LintRuleViolation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public LintRuleViolation severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Integer getLineStart() {
        return lineStart;
    }

    public LintRuleViolation lineStart(Integer lineStart) {
        this.lineStart = lineStart;
        return this;
    }

    public void setLineStart(Integer lineStart) {
        this.lineStart = lineStart;
    }

    public Integer getLindEnd() {
        return lindEnd;
    }

    public LintRuleViolation lindEnd(Integer lindEnd) {
        this.lindEnd = lindEnd;
        return this;
    }

    public void setLindEnd(Integer lindEnd) {
        this.lindEnd = lindEnd;
    }

    public Integer getColumnStart() {
        return columnStart;
    }

    public LintRuleViolation columnStart(Integer columnStart) {
        this.columnStart = columnStart;
        return this;
    }

    public void setColumnStart(Integer columnStart) {
        this.columnStart = columnStart;
    }

    public Integer getColumnEnd() {
        return columnEnd;
    }

    public LintRuleViolation columnEnd(Integer columnEnd) {
        this.columnEnd = columnEnd;
        return this;
    }

    public void setColumnEnd(Integer columnEnd) {
        this.columnEnd = columnEnd;
    }

    public String getJsonPointer() {
        return jsonPointer;
    }

    public LintRuleViolation jsonPointer(String jsonPointer) {
        this.jsonPointer = jsonPointer;
        return this;
    }

    public void setJsonPointer(String jsonPointer) {
        this.jsonPointer = jsonPointer;
    }

    public LintRule getLintRule() {
        return lintRule;
    }

    public LintRuleViolation lintRule(LintRule lintRule) {
        this.lintRule = lintRule;
        return this;
    }

    public void setLintRule(LintRule lintRule) {
        this.lintRule = lintRule;
    }

    public Set<LintReport> getLintReports() {
        return lintReports;
    }

    public LintRuleViolation lintReports(Set<LintReport> lintReports) {
        this.lintReports = lintReports;
        return this;
    }

    public LintRuleViolation addLintReport(LintReport lintReport) {
        this.lintReports.add(lintReport);
        lintReport.setLinkRuleViolation(this);
        return this;
    }

    public LintRuleViolation removeLintReport(LintReport lintReport) {
        this.lintReports.remove(lintReport);
        lintReport.setLinkRuleViolation(null);
        return this;
    }

    public void setLintReports(Set<LintReport> lintReports) {
        this.lintReports = lintReports;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LintRuleViolation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", severity='" + getSeverity() + "'" +
            ", lineStart=" + getLineStart() +
            ", lindEnd=" + getLindEnd() +
            ", columnStart=" + getColumnStart() +
            ", columnEnd=" + getColumnEnd() +
            ", jsonPointer='" + getJsonPointer() + "'" +
            "}";
    }
}
