package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "hide")
    private Boolean hide;

    @Column(name = "color")
    private String color;

    @ManyToMany(mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tag id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Tag description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public Tag hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getColor() {
        return this.color;
    }

    public Tag color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Spec> getSpecs() {
        return this.specs;
    }

    public Tag specs(Set<Spec> specs) {
        this.setSpecs(specs);
        return this;
    }

    public Tag addSpec(Spec spec) {
        this.specs.add(spec);
        spec.getTags().add(this);
        return this;
    }

    public Tag removeSpec(Spec spec) {
        this.specs.remove(spec);
        spec.getTags().remove(this);
        return this;
    }

    public void setSpecs(Set<Spec> specs) {
        if (this.specs != null) {
            this.specs.forEach(i -> i.removeTag(this));
        }
        if (specs != null) {
            specs.forEach(i -> i.addTag(this));
        }
        this.specs = specs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hide='" + getHide() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
