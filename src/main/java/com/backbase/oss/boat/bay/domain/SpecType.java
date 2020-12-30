package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SpecType.
 */
@Entity
@Table(name = "spec_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "match_sp_el", nullable = false)
    private String matchSpEL;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

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

    public SpecType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SpecType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatchSpEL() {
        return matchSpEL;
    }

    public SpecType matchSpEL(String matchSpEL) {
        this.matchSpEL = matchSpEL;
        return this;
    }

    public void setMatchSpEL(String matchSpEL) {
        this.matchSpEL = matchSpEL;
    }

    public String getIcon() {
        return icon;
    }

    public SpecType icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecType)) {
            return false;
        }
        return id != null && id.equals(((SpecType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", matchSpEL='" + getMatchSpEL() + "'" +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
