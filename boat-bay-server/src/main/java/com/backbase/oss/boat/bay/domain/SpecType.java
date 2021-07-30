package com.backbase.oss.boat.bay.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "match_sp_el")
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

    public SpecType id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SpecType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SpecType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatchSpEL() {
        return this.matchSpEL;
    }

    public SpecType matchSpEL(String matchSpEL) {
        this.matchSpEL = matchSpEL;
        return this;
    }

    public void setMatchSpEL(String matchSpEL) {
        this.matchSpEL = matchSpEL;
    }

    public String getIcon() {
        return this.icon;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
