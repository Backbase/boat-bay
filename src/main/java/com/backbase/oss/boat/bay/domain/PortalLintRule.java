package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "portalLintRule")
    @JsonIgnore
    private PortalLintRuleConfig portalLintRuleConfig;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "portalLintRules", allowSetters = true)
    private PortalLintRuleSet portalRuleSet;

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

    public PortalLintRule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortalLintRuleConfig getPortalLintRuleConfig() {
        return portalLintRuleConfig;
    }

    public PortalLintRule portalLintRuleConfig(PortalLintRuleConfig portalLintRuleConfig) {
        this.portalLintRuleConfig = portalLintRuleConfig;
        return this;
    }

    public void setPortalLintRuleConfig(PortalLintRuleConfig portalLintRuleConfig) {
        this.portalLintRuleConfig = portalLintRuleConfig;
    }

    public PortalLintRuleSet getPortalRuleSet() {
        return portalRuleSet;
    }

    public PortalLintRule portalRuleSet(PortalLintRuleSet portalLintRuleSet) {
        this.portalRuleSet = portalLintRuleSet;
        return this;
    }

    public void setPortalRuleSet(PortalLintRuleSet portalLintRuleSet) {
        this.portalRuleSet = portalLintRuleSet;
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
            ", name='" + getName() + "'" +
            "}";
    }
}
