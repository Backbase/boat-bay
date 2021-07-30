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
    @JoinTable(
        name = "rel_product_release__spec",
        joinColumns = @JoinColumn(name = "product_release_id"),
        inverseJoinColumns = @JoinColumn(name = "spec_id")
    )
    @JsonIgnoreProperties(
        value = {
            "previousSpec",
            "portal",
            "capability",
            "product",
            "source",
            "specType",
            "tags",
            "lintReport",
            "successor",
            "serviceDefinition",
            "productReleases",
        },
        allowSetters = true
    )
    private Set<Spec> specs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productReleases", "capabilities", "portal" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductRelease id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public ProductRelease key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public ProductRelease name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public ProductRelease version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ZonedDateTime getReleaseDate() {
        return this.releaseDate;
    }

    public ProductRelease releaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public ProductRelease hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Set<Spec> getSpecs() {
        return this.specs;
    }

    public ProductRelease specs(Set<Spec> specs) {
        this.setSpecs(specs);
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
        return this.product;
    }

    public ProductRelease product(Product product) {
        this.setProduct(product);
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
            ", hide='" + getHide() + "'" +
            "}";
    }
}
