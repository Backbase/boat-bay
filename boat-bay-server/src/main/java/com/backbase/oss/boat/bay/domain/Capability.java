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
 * A Capability.
 */
@Entity
@Table(name = "capability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Capability implements Serializable {

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
    @Column(name = "content")
    private String content;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "hide")
    private Boolean hide;

    @OneToMany(mappedBy = "capability")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ServiceDefinition> serviceDefinitions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "capabilities", allowSetters = true)
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

    public Capability key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public Capability name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public Capability order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Capability subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public Capability content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Capability createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Capability createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isHide() {
        return hide;
    }

    public Capability hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Set<ServiceDefinition> getServiceDefinitions() {
        return serviceDefinitions;
    }

    public Capability serviceDefinitions(Set<ServiceDefinition> serviceDefinitions) {
        this.serviceDefinitions = serviceDefinitions;
        return this;
    }

    public Capability addServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinitions.add(serviceDefinition);
        serviceDefinition.setCapability(this);
        return this;
    }

    public Capability removeServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinitions.remove(serviceDefinition);
        serviceDefinition.setCapability(null);
        return this;
    }

    public void setServiceDefinitions(Set<ServiceDefinition> serviceDefinitions) {
        this.serviceDefinitions = serviceDefinitions;
    }

    public Product getProduct() {
        return product;
    }

    public Capability product(Product product) {
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
        if (!(o instanceof Capability)) {
            return false;
        }
        return id != null && id.equals(((Capability) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Capability{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", subTitle='" + getSubTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", hide='" + isHide() + "'" +
            "}";
    }
}
