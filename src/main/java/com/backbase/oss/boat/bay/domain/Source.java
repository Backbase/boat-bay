package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.backbase.oss.boat.bay.domain.enumeration.SourceType;

/**
 * A Source.
 */
@Entity
@Table(name = "source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Source implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SourceType type;

    @NotNull
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "path")
    private String path;

    @Column(name = "filter")
    private String filter;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "spec_filter_sp_el")
    private String specFilterSpEL;

    @Column(name = "product_key_sp_el")
    private String productKeySpEL;

    @Column(name = "product_name_sp_el")
    private String productNameSpEL;

    @Column(name = "capability_key_sp_el")
    private String capabilityKeySpEL;

    @Column(name = "capability_name_sp_el")
    private String capabilityNameSpEL;

    @Column(name = "service_key_sp_el")
    private String serviceKeySpEL;

    @Column(name = "service_name_sp_el")
    private String serviceNameSpEL;

    @Column(name = "version_sp_el")
    private String versionSpEL;

    @Column(name = "overwrite_changes")
    private Boolean overwriteChanges;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Portal portal;

    @OneToOne
    @JoinColumn(unique = true)
    private Capability capability;

    @OneToOne
    @JoinColumn(unique = true)
    private ServiceDefinition serviceDefinition;

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

    public Source name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SourceType getType() {
        return type;
    }

    public Source type(SourceType type) {
        this.type = type;
        return this;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Source baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public Source active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPath() {
        return path;
    }

    public Source path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilter() {
        return filter;
    }

    public Source filter(String filter) {
        this.filter = filter;
        return this;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUsername() {
        return username;
    }

    public Source username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public Source password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public Source cronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getSpecFilterSpEL() {
        return specFilterSpEL;
    }

    public Source specFilterSpEL(String specFilterSpEL) {
        this.specFilterSpEL = specFilterSpEL;
        return this;
    }

    public void setSpecFilterSpEL(String specFilterSpEL) {
        this.specFilterSpEL = specFilterSpEL;
    }

    public String getProductKeySpEL() {
        return productKeySpEL;
    }

    public Source productKeySpEL(String productKeySpEL) {
        this.productKeySpEL = productKeySpEL;
        return this;
    }

    public void setProductKeySpEL(String productKeySpEL) {
        this.productKeySpEL = productKeySpEL;
    }

    public String getProductNameSpEL() {
        return productNameSpEL;
    }

    public Source productNameSpEL(String productNameSpEL) {
        this.productNameSpEL = productNameSpEL;
        return this;
    }

    public void setProductNameSpEL(String productNameSpEL) {
        this.productNameSpEL = productNameSpEL;
    }

    public String getCapabilityKeySpEL() {
        return capabilityKeySpEL;
    }

    public Source capabilityKeySpEL(String capabilityKeySpEL) {
        this.capabilityKeySpEL = capabilityKeySpEL;
        return this;
    }

    public void setCapabilityKeySpEL(String capabilityKeySpEL) {
        this.capabilityKeySpEL = capabilityKeySpEL;
    }

    public String getCapabilityNameSpEL() {
        return capabilityNameSpEL;
    }

    public Source capabilityNameSpEL(String capabilityNameSpEL) {
        this.capabilityNameSpEL = capabilityNameSpEL;
        return this;
    }

    public void setCapabilityNameSpEL(String capabilityNameSpEL) {
        this.capabilityNameSpEL = capabilityNameSpEL;
    }

    public String getServiceKeySpEL() {
        return serviceKeySpEL;
    }

    public Source serviceKeySpEL(String serviceKeySpEL) {
        this.serviceKeySpEL = serviceKeySpEL;
        return this;
    }

    public void setServiceKeySpEL(String serviceKeySpEL) {
        this.serviceKeySpEL = serviceKeySpEL;
    }

    public String getServiceNameSpEL() {
        return serviceNameSpEL;
    }

    public Source serviceNameSpEL(String serviceNameSpEL) {
        this.serviceNameSpEL = serviceNameSpEL;
        return this;
    }

    public void setServiceNameSpEL(String serviceNameSpEL) {
        this.serviceNameSpEL = serviceNameSpEL;
    }

    public String getVersionSpEL() {
        return versionSpEL;
    }

    public Source versionSpEL(String versionSpEL) {
        this.versionSpEL = versionSpEL;
        return this;
    }

    public void setVersionSpEL(String versionSpEL) {
        this.versionSpEL = versionSpEL;
    }

    public Boolean isOverwriteChanges() {
        return overwriteChanges;
    }

    public Source overwriteChanges(Boolean overwriteChanges) {
        this.overwriteChanges = overwriteChanges;
        return this;
    }

    public void setOverwriteChanges(Boolean overwriteChanges) {
        this.overwriteChanges = overwriteChanges;
    }

    public Portal getPortal() {
        return portal;
    }

    public Source portal(Portal portal) {
        this.portal = portal;
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Capability getCapability() {
        return capability;
    }

    public Source capability(Capability capability) {
        this.capability = capability;
        return this;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public Source serviceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
        return this;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        return id != null && id.equals(((Source) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Source{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", baseUrl='" + getBaseUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", path='" + getPath() + "'" +
            ", filter='" + getFilter() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", cronExpression='" + getCronExpression() + "'" +
            ", specFilterSpEL='" + getSpecFilterSpEL() + "'" +
            ", productKeySpEL='" + getProductKeySpEL() + "'" +
            ", productNameSpEL='" + getProductNameSpEL() + "'" +
            ", capabilityKeySpEL='" + getCapabilityKeySpEL() + "'" +
            ", capabilityNameSpEL='" + getCapabilityNameSpEL() + "'" +
            ", serviceKeySpEL='" + getServiceKeySpEL() + "'" +
            ", serviceNameSpEL='" + getServiceNameSpEL() + "'" +
            ", versionSpEL='" + getVersionSpEL() + "'" +
            ", overwriteChanges='" + isOverwriteChanges() + "'" +
            "}";
    }
}
