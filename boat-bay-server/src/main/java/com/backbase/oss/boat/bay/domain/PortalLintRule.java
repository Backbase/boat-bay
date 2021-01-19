package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PortalLintRule.
 */
@Entity
@Table(name = "portal_lint_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PortalLintRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "portalLintRules", allowSetters = true)
    private LintRule lintRule;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "portalLintRules", allowSetters = true)
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

    public PortalLintRule ruleId(String ruleId) {
        this.ruleId = ruleId;
        return this;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public PortalLintRule enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LintRule getLintRule() {
        return lintRule;
    }

    public PortalLintRule lintRule(LintRule lintRule) {
        this.lintRule = lintRule;
        return this;
    }

    public void setLintRule(LintRule lintRule) {
        this.lintRule = lintRule;
    }

    public Portal getPortal() {
        return portal;
    }

    public PortalLintRule portal(Portal portal) {
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
        if (!(o instanceof PortalLintRule)) {
            return false;
        }
        return id != null && id.equals(((PortalLintRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortalLintRule{" +
            "id=" + getId() +
            ", ruleId='" + getRuleId() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
