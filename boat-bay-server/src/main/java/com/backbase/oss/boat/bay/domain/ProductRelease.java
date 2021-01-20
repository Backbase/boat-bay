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
 * A ProductRelease.
 */
@Entity
@Table(name = "product_release")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductRelease implements Serializable {

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

    @NotNull
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "release_date")
    private ZonedDateTime releaseDate;

    @Column(name = "hide")
    private Boolean hide;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "product_release_spec",
               joinColumns = @JoinColumn(name = "product_release_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "spec_id", referencedColumnName = "id"))
    private Set<Spec> specs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "productReleases", allowSetters = true)
    private Product product;

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

    public ProductRelease key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public ProductRelease name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public ProductRelease version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public ProductRelease releaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isHide() {
        return hide;
    }

    public ProductRelease hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Set<Spec> getSpecs() {
        return specs;
    }

    public ProductRelease specs(Set<Spec> specs) {
        this.specs = specs;
        return this;
    }

    public ProductRelease addSpec(Spec spec) {
        this.specs.add(spec);
        spec.getProductReleases().add(this);
        return this;
    }

    public ProductRelease removeSpec(Spec spec) {
        this.specs.remove(spec);
        spec.getProductReleases().remove(this);
        return this;
    }

    public void setSpecs(Set<Spec> specs) {
        this.specs = specs;
    }

    public Product getProduct() {
        return product;
    }

    public ProductRelease product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductRelease)) {
            return false;
        }
        return id != null && id.equals(((ProductRelease) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductRelease{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", version='" + getVersion() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", hide='" + isHide() + "'" +
            "}";
    }
}