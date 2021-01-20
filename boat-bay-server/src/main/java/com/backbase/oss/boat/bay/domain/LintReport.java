package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "name")
    private String name;

    @Column(name = "grade")
    private String grade;

    @Column(name = "passed")
    private Boolean passed;

    @Column(name = "linted_on")
    private ZonedDateTime lintedOn;

    @OneToOne
    @JoinColumn(unique = true)
    private Spec spec;

    @OneToMany(mappedBy = "lintReport")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LintRuleViolation> violations = new HashSet<>();

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

    public LintReport name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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

    public ZonedDateTime getLintedOn() {
        return lintedOn;
    }

    public LintReport lintedOn(ZonedDateTime lintedOn) {
        this.lintedOn = lintedOn;
        return this;
    }

    public void setLintedOn(ZonedDateTime lintedOn) {
        this.lintedOn = lintedOn;
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

    public Set<LintRuleViolation> getViolations() {
        return violations;
    }

    public LintReport violations(Set<LintRuleViolation> lintRuleViolations) {
        this.violations = lintRuleViolations;
        return this;
    }

    public LintReport addViolations(LintRuleViolation lintRuleViolation) {
        this.violations.add(lintRuleViolation);
        lintRuleViolation.setLintReport(this);
        return this;
    }

    public LintReport removeViolations(LintRuleViolation lintRuleViolation) {
        this.violations.remove(lintRuleViolation);
        lintRuleViolation.setLintReport(null);
        return this;
    }

    public void setViolations(Set<LintRuleViolation> lintRuleViolations) {
        this.violations = lintRuleViolations;
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
            ", name='" + getName() + "'" +
            ", grade='" + getGrade() + "'" +
            ", passed='" + isPassed() + "'" +
            ", lintedOn='" + getLintedOn() + "'" +
            "}";
    }
}
