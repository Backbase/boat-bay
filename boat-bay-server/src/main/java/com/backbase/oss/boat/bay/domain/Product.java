package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @JsonIgnoreProperties(value = { "specs", "product" }, allowSetters = true)
    private Set<ProductRelease> productReleases = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "serviceDefinitions", "product" }, allowSetters = true)
    private Set<Capability> capabilities = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "lintRules", "zallyConfig" }, allowSetters = true)
    private Portal portal;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public Product key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Product order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getContent() {
        return this.content;
    }

    public Product content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedOn() {
        return this.createdOn;
    }

    public Product createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public Product hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getJiraProjectId() {
        return this.jiraProjectId;
    }

    public Product jiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
        return this;
    }

    public void setJiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public Set<ProductRelease> getProductReleases() {
        return this.productReleases;
    }

    public Product productReleases(Set<ProductRelease> productReleases) {
        this.setProductReleases(productReleases);
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
        if (this.productReleases != null) {
            this.productReleases.forEach(i -> i.setProduct(null));
        }
        if (productReleases != null) {
            productReleases.forEach(i -> i.setProduct(this));
        }
        this.productReleases = productReleases;
    }

    public Set<Capability> getCapabilities() {
        return this.capabilities;
    }

    public Product capabilities(Set<Capability> capabilities) {
        this.setCapabilities(capabilities);
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
        if (this.capabilities != null) {
            this.capabilities.forEach(i -> i.setProduct(null));
        }
        if (capabilities != null) {
            capabilities.forEach(i -> i.setProduct(this));
        }
        this.capabilities = capabilities;
    }

    public Portal getPortal() {
        return this.portal;
    }

    public Product portal(Portal portal) {
        this.setPortal(portal);
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
            ", hide='" + getHide() + "'" +
            ", jiraProjectId='" + getJiraProjectId() + "'" +
            "}";
    }
}
