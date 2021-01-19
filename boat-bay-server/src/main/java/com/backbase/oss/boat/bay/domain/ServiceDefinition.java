package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceDefinition.
 */
@Entity
@Table(name = "service_definition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceDefinition implements Serializable {

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

    @Column(name = "sub_title")
    private String subTitle;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "icon")
    private String icon;

    @Column(name = "color")
    private String color;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "hide")
    private Boolean hide;

    @OneToMany(mappedBy = "serviceDefinition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Spec> specs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "serviceDefinitions", allowSetters = true)
    private Capability capability;

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

    public ServiceDefinition key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public ServiceDefinition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public ServiceDefinition order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public ServiceDefinition subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public ServiceDefinition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public ServiceDefinition icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public ServiceDefinition color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public ServiceDefinition createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ServiceDefinition createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isHide() {
        return hide;
    }

    public ServiceDefinition hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Set<Spec> getSpecs() {
        return specs;
    }

    public ServiceDefinition specs(Set<Spec> specs) {
        this.specs = specs;
        return this;
    }

    public ServiceDefinition addSpec(Spec spec) {
        this.specs.add(spec);
        spec.setServiceDefinition(this);
        return this;
    }

    public ServiceDefinition removeSpec(Spec spec) {
        this.specs.remove(spec);
        spec.setServiceDefinition(null);
        return this;
    }

    public void setSpecs(Set<Spec> specs) {
        this.specs = specs;
    }

    public Capability getCapability() {
        return capability;
    }

    public ServiceDefinition capability(Capability capability) {
        this.capability = capability;
        return this;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceDefinition)) {
            return false;
        }
        return id != null && id.equals(((ServiceDefinition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceDefinition{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", subTitle='" + getSubTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", icon='" + getIcon() + "'" +
            ", color='" + getColor() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", hide='" + isHide() + "'" +
            "}";
    }
}
