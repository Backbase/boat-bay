package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PortalLintRuleConfig.
 */
@Entity
@Table(name = "portal_lint_rule_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PortalLintRuleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    
    @Lob
    @Column(name = "value", nullable = false)
    private String value;

    @OneToOne
    @JoinColumn(unique = true)
    private PortalLintRule portalLintRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public PortalLintRuleConfig path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public PortalLintRuleConfig type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public PortalLintRuleConfig value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PortalLintRule getPortalLintRule() {
        return portalLintRule;
    }

    public PortalLintRuleConfig portalLintRule(PortalLintRule portalLintRule) {
        this.portalLintRule = portalLintRule;
        return this;
    }

    public void setPortalLintRule(PortalLintRule portalLintRule) {
        this.portalLintRule = portalLintRule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortalLintRuleConfig)) {
            return false;
        }
        return id != null && id.equals(((PortalLintRuleConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortalLintRuleConfig{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
