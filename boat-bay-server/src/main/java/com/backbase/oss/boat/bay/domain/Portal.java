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

    @NotNull
    @Column(name = "jhi_key", nullable = false, unique = true)
    private String key;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "logo_link")
    private String logoLink;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "hide")
    private Boolean hide;

    @Column(name = "linted")
    private Boolean linted;

    @OneToMany(mappedBy = "portal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productReleases", "capabilities", "portal" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "portal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "portal" }, allowSetters = true)
    private Set<LintRule> lintRules = new HashSet<>();

    @JsonIgnoreProperties(value = { "portal" }, allowSetters = true)
    @OneToOne(mappedBy = "portal")
    private ZallyConfig zallyConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Portal id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public Portal key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public Portal name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public Portal subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public Portal logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoLink() {
        return this.logoLink;
    }

    public Portal logoLink(String logoLink) {
        this.logoLink = logoLink;
        return this;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getContent() {
        return this.content;
    }

    public Portal content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedOn() {
        return this.createdOn;
    }

    public Portal createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Portal createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public Portal hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Boolean getLinted() {
        return this.linted;
    }

    public Portal linted(Boolean linted) {
        this.linted = linted;
        return this;
    }

    public void setLinted(Boolean linted) {
        this.linted = linted;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Portal products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Portal addProduct(Product product) {
        this.products.add(product);
        product.setPortal(this);
        return this;
    }

    public Portal removeProduct(Product product) {
        this.products.remove(product);
        product.setPortal(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setPortal(null));
        }
        if (products != null) {
            products.forEach(i -> i.setPortal(this));
        }
        this.products = products;
    }

    public Set<LintRule> getLintRules() {
        return this.lintRules;
    }

    public Portal lintRules(Set<LintRule> lintRules) {
        this.setLintRules(lintRules);
        return this;
    }

    public Portal addLintRule(LintRule lintRule) {
        this.lintRules.add(lintRule);
        lintRule.setPortal(this);
        return this;
    }

    public Portal removeLintRule(LintRule lintRule) {
        this.lintRules.remove(lintRule);
        lintRule.setPortal(null);
        return this;
    }

    public void setLintRules(Set<LintRule> lintRules) {
        if (this.lintRules != null) {
            this.lintRules.forEach(i -> i.setPortal(null));
        }
        if (lintRules != null) {
            lintRules.forEach(i -> i.setPortal(this));
        }
        this.lintRules = lintRules;
    }

    public ZallyConfig getZallyConfig() {
        return this.zallyConfig;
    }

    public Portal zallyConfig(ZallyConfig zallyConfig) {
        this.setZallyConfig(zallyConfig);
        return this;
    }

    public void setZallyConfig(ZallyConfig zallyConfig) {
        if (this.zallyConfig != null) {
            this.zallyConfig.setPortal(null);
        }
        if (zallyConfig != null) {
            zallyConfig.setPortal(this);
        }
        this.zallyConfig = zallyConfig;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Portal{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", logoLink='" + getLogoLink() + "'" +
            ", content='" + getContent() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", hide='" + getHide() + "'" +
            ", linted='" + getLinted() + "'" +
            "}";
    }
}
