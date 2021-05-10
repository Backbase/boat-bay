package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ZallyConfig.
 */
@Entity
@Table(name = "zally_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ZallyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "contents")
    private String contents;

    @OneToOne
    @JoinColumn(unique = true)
    private Portal portal;

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

    public ZallyConfig name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public ZallyConfig contents(String contents) {
        this.contents = contents;
        return this;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Portal getPortal() {
        return portal;
    }

    public ZallyConfig portal(Portal portal) {
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
        if (!(o instanceof ZallyConfig)) {
            return false;
        }
        return id != null && id.equals(((ZallyConfig) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZallyConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contents='" + getContents() + "'" +
            "}";
    }
}
