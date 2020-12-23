package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Portal.
 */
@Entity
@Table(name = "portal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Portal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "title")
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "nav_title")
    private String navTitle;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "logo_link")
    private String logoLink;

    @Column(name = "content")
    private String content;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "portal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Capability> capabilities = new HashSet<>();

    @OneToMany(mappedBy = "portal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Service> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public Portal key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public Portal title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Portal subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public Portal navTitle(String navTitle) {
        this.navTitle = navTitle;
        return this;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Portal logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public Portal logoLink(String logoLink) {
        this.logoLink = logoLink;
        return this;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getContent() {
        return content;
    }

    public Portal content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Portal createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Portal createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Capability> getCapabilities() {
        return capabilities;
    }

    public Portal capabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public Portal addCapability(Capability capability) {
        this.capabilities.add(capability);
        capability.setPortal(this);
        return this;
    }

    public Portal removeCapability(Capability capability) {
        this.capabilities.remove(capability);
        capability.setPortal(null);
        return this;
    }

    public void setCapabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public Set<Service> getServices() {
        return services;
    }

    public Portal services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Portal addService(Service service) {
        this.services.add(service);
        service.setPortal(this);
        return this;
    }

    public Portal removeService(Service service) {
        this.services.remove(service);
        service.setPortal(null);
        return this;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Portal)) {
            return false;
        }
        return id != null && id.equals(((Portal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Portal{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", navTitle='" + getNavTitle() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", logoLink='" + getLogoLink() + "'" +
            ", content='" + getContent() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
