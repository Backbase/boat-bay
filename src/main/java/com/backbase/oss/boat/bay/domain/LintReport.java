package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A LintReport.
 */
@Entity
@Table(name = "lint_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LintReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade")
    private String grade;

    @Column(name = "passed")
    private Boolean passed;

    @OneToOne(mappedBy = "lintReport")
    @JsonIgnore
    private Spec spec;

    @ManyToOne
    @JsonIgnoreProperties(value = "lintReports", allowSetters = true)
    private LintRuleViolation linkRuleViolation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public LintReport grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Boolean isPassed() {
        return passed;
    }

    public LintReport passed(Boolean passed) {
        this.passed = passed;
        return this;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Spec getSpec() {
        return spec;
    }

    public LintReport spec(Spec spec) {
        this.spec = spec;
        return this;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public LintRuleViolation getLinkRuleViolation() {
        return linkRuleViolation;
    }

    public LintReport linkRuleViolation(LintRuleViolation lintRuleViolation) {
        this.linkRuleViolation = lintRuleViolation;
        return this;
    }

    public void setLinkRuleViolation(LintRuleViolation lintRuleViolation) {
        this.linkRuleViolation = lintRuleViolation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LintReport)) {
            return false;
        }
        return id != null && id.equals(((LintReport) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LintReport{" +
            "id=" + getId() +
            ", grade='" + getGrade() + "'" +
            ", passed='" + isPassed() + "'" +
            "}";
    }
}
