package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SourcePath.
 */
@Entity
@Table(name = "source_path")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SourcePath implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sourcePaths", "portal", "product", "capability", "serviceDefinition" }, allowSetters = true)
    private Source source;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SourcePath id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SourcePath name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Source getSource() {
        return this.source;
    }

    public SourcePath source(Source source) {
        this.setSource(source);
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourcePath)) {
            return false;
        }
        return id != null && id.equals(((SourcePath) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourcePath{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
