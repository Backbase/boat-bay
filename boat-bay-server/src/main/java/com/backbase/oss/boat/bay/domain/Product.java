package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_order")
    private Integer order;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "hide")
    private Boolean hide;

    @Column(name = "jira_project_id")
    private String jiraProjectId;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProductRelease> productReleases = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Capability> capabilities = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "products", allowSetters = true)
    private Portal portal;

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

    public Product key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public Product order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public Product content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public Product createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Product createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isHide() {
        return hide;
    }

    public Product hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getJiraProjectId() {
        return jiraProjectId;
    }

    public Product jiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
        return this;
    }

    public void setJiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public Set<ProductRelease> getProductReleases() {
        return productReleases;
    }

    public Product productReleases(Set<ProductRelease> productReleases) {
        this.productReleases = productReleases;
        return this;
    }

    public Product addProductRelease(ProductRelease productRelease) {
        this.productReleases.add(productRelease);
        productRelease.setProduct(this);
        return this;
    }

    public Product removeProductRelease(ProductRelease productRelease) {
        this.productReleases.remove(productRelease);
        productRelease.setProduct(null);
        return this;
    }

    public void setProductReleases(Set<ProductRelease> productReleases) {
        this.productReleases = productReleases;
    }

    public Set<Capability> getCapabilities() {
        return capabilities;
    }

    public Product capabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public Product addCapability(Capability capability) {
        this.capabilities.add(capability);
        capability.setProduct(this);
        return this;
    }

    public Product removeCapability(Capability capability) {
        this.capabilities.remove(capability);
        capability.setProduct(null);
        return this;
    }

    public void setCapabilities(Set<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public Portal getPortal() {
        return portal;
    }

    public Product portal(Portal portal) {
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
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", content='" + getContent() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", hide='" + isHide() + "'" +
            ", jiraProjectId='" + getJiraProjectId() + "'" +
            "}";
    }
}