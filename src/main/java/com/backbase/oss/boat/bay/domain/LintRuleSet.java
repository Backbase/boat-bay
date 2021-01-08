package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A LintRuleSet.
 */
@Entity
@Table(name = "lint_rule_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LintRuleSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rule_set_id", nullable = false, unique = true)
    private String ruleSetId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "external_url")
    private String externalUrl;

    @OneToMany(mappedBy = "ruleSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LintRule> lintRules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleSetId() {
        return ruleSetId;
    }

    public LintRuleSet ruleSetId(String ruleSetId) {
        this.ruleSetId = ruleSetId;
        return this;
    }

    public void setRuleSetId(String ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public String getName() {
        return name;
    }

    public LintRuleSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public LintRuleSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public LintRuleSet externalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Set<LintRule> getLintRules() {
        return lintRules;
    }

    public LintRuleSet lintRules(Set<LintRule> lintRules) {
        this.lintRules = lintRules;
        return this;
    }

    public LintRuleSet addLintRule(LintRule lintRule) {
        this.lintRules.add(lintRule);
        lintRule.setRuleSet(this);
        return this;
    }

    public LintRuleSet removeLintRule(LintRule lintRule) {
        this.lintRules.remove(lintRule);
        lintRule.setRuleSet(null);
        return this;
    }

    public void setLintRules(Set<LintRule> lintRules) {
        this.lintRules = lintRules;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LintRuleSet)) {
            return false;
        }
        return id != null && id.equals(((LintRuleSet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LintRuleSet{" +
            "id=" + getId() +
            ", ruleSetId='" + getRuleSetId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", externalUrl='" + getExternalUrl() + "'" +
            "}";
    }
}
