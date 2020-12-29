package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PortalLintRuleSet.
 */
@Entity
@Table(name = "portal_lint_rule_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PortalLintRuleSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Portal portal;

    @OneToMany(mappedBy = "portalRuleSet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PortalLintRule> portalLintRules = new HashSet<>();

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

    public PortalLintRuleSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Portal getPortal() {
        return portal;
    }

    public PortalLintRuleSet portal(Portal portal) {
        this.portal = portal;
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Set<PortalLintRule> getPortalLintRules() {
        return portalLintRules;
    }

    public PortalLintRuleSet portalLintRules(Set<PortalLintRule> portalLintRules) {
        this.portalLintRules = portalLintRules;
        return this;
    }

    public PortalLintRuleSet addPortalLintRule(PortalLintRule portalLintRule) {
        this.portalLintRules.add(portalLintRule);
        portalLintRule.setPortalRuleSet(this);
        return this;
    }

    public PortalLintRuleSet removePortalLintRule(PortalLintRule portalLintRule) {
        this.portalLintRules.remove(portalLintRule);
        portalLintRule.setPortalRuleSet(null);
        return this;
    }

    public void setPortalLintRules(Set<PortalLintRule> portalLintRules) {
        this.portalLintRules = portalLintRules;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortalLintRuleSet)) {
            return false;
        }
        return id != null && id.equals(((PortalLintRuleSet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortalLintRuleSet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
