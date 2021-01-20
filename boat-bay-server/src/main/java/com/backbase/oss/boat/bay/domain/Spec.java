package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Spec.
 */
@Entity
@Table(name = "spec")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spec implements Serializable {

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

    @Column(name = "title")
    private String title;

    @Column(name = "icon")
    private String icon;


    @Lob
    @Column(name = "open_api", nullable = false)
    private String openApi;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private ZonedDateTime createdOn;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "checksum", nullable = false)
    private String checksum;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Column(name = "valid", nullable = false)
    private Boolean valid;

    @Column(name = "jhi_order")
    private Integer order;

    @Lob
    @Column(name = "parse_error")
    private String parseError;

    @Column(name = "external_docs")
    private String externalDocs;

    @Column(name = "hide")
    private Boolean hide;

    @Column(name = "grade")
    private String grade;

    @Column(name = "backwards_compatible")
    private Boolean backwardsCompatible;

    @Column(name = "changed")
    private Boolean changed;

    @Column(name = "source_path")
    private String sourcePath;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "source_created_by")
    private String sourceCreatedBy;

    @Column(name = "source_created_on")
    private ZonedDateTime sourceCreatedOn;

    @Column(name = "source_last_modified_on")
    private ZonedDateTime sourceLastModifiedOn;

    @Column(name = "source_last_modified_by")
    private String sourceLastModifiedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Portal portal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Capability capability;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Source source;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private SpecType specType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "spec_tag",
               joinColumns = @JoinColumn(name = "spec_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToOne(mappedBy = "spec")
    @JsonIgnore
    private LintReport lintReport;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private ServiceDefinition serviceDefinition;

    @ManyToMany(mappedBy = "specs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<ProductRelease> productReleases = new HashSet<>();

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

    public Spec key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public Spec name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public Spec version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public Spec title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public Spec icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenApi() {
        return openApi;
    }

    public Spec openApi(String openApi) {
        this.openApi = openApi;
        return this;
    }

    public void setOpenApi(String openApi) {
        this.openApi = openApi;
    }

    public String getDescription() {
        return description;
    }

    public Spec description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public Spec createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Spec createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getChecksum() {
        return checksum;
    }

    public Spec checksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getFilename() {
        return filename;
    }

    public Spec filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean isValid() {
        return valid;
    }

    public Spec valid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getOrder() {
        return order;
    }

    public Spec order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getParseError() {
        return parseError;
    }

    public Spec parseError(String parseError) {
        this.parseError = parseError;
        return this;
    }

    public void setParseError(String parseError) {
        this.parseError = parseError;
    }

    public String getExternalDocs() {
        return externalDocs;
    }

    public Spec externalDocs(String externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    public void setExternalDocs(String externalDocs) {
        this.externalDocs = externalDocs;
    }

    public Boolean isHide() {
        return hide;
    }

    public Spec hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getGrade() {
        return grade;
    }

    public Spec grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Boolean isBackwardsCompatible() {
        return backwardsCompatible;
    }

    public Spec backwardsCompatible(Boolean backwardsCompatible) {
        this.backwardsCompatible = backwardsCompatible;
        return this;
    }

    public void setBackwardsCompatible(Boolean backwardsCompatible) {
        this.backwardsCompatible = backwardsCompatible;
    }

    public Boolean isChanged() {
        return changed;
    }

    public Spec changed(Boolean changed) {
        this.changed = changed;
        return this;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public Spec sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Spec sourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Spec sourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceCreatedBy() {
        return sourceCreatedBy;
    }

    public Spec sourceCreatedBy(String sourceCreatedBy) {
        this.sourceCreatedBy = sourceCreatedBy;
        return this;
    }

    public void setSourceCreatedBy(String sourceCreatedBy) {
        this.sourceCreatedBy = sourceCreatedBy;
    }

    public ZonedDateTime getSourceCreatedOn() {
        return sourceCreatedOn;
    }

    public Spec sourceCreatedOn(ZonedDateTime sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
        return this;
    }

    public void setSourceCreatedOn(ZonedDateTime sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
    }

    public ZonedDateTime getSourceLastModifiedOn() {
        return sourceLastModifiedOn;
    }

    public Spec sourceLastModifiedOn(ZonedDateTime sourceLastModifiedOn) {
        this.sourceLastModifiedOn = sourceLastModifiedOn;
        return this;
    }

    public void setSourceLastModifiedOn(ZonedDateTime sourceLastModifiedOn) {
        this.sourceLastModifiedOn = sourceLastModifiedOn;
    }

    public String getSourceLastModifiedBy() {
        return sourceLastModifiedBy;
    }

    public Spec sourceLastModifiedBy(String sourceLastModifiedBy) {
        this.sourceLastModifiedBy = sourceLastModifiedBy;
        return this;
    }

    public void setSourceLastModifiedBy(String sourceLastModifiedBy) {
        this.sourceLastModifiedBy = sourceLastModifiedBy;
    }

    public Portal getPortal() {
        return portal;
    }

    public Spec portal(Portal portal) {
        this.portal = portal;
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Capability getCapability() {
        return capability;
    }

    public Spec capability(Capability capability) {
        this.capability = capability;
        return this;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public Product getProduct() {
        return product;
    }

    public Spec product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Source getSource() {
        return source;
    }

    public Spec source(Source source) {
        this.source = source;
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SpecType getSpecType() {
        return specType;
    }

    public Spec specType(SpecType specType) {
        this.specType = specType;
        return this;
    }

    public void setSpecType(SpecType specType) {
        this.specType = specType;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Spec tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Spec addTag(Tag tag) {
        this.tags.add(tag);
        tag.getSpecs().add(this);
        return this;
    }

    public Spec removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getSpecs().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public LintReport getLintReport() {
        return lintReport;
    }

    public Spec lintReport(LintReport lintReport) {
        this.lintReport = lintReport;
        return this;
    }

    public void setLintReport(LintReport lintReport) {
        this.lintReport = lintReport;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public Spec serviceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
        return this;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    public Set<ProductRelease> getProductReleases() {
        return productReleases;
    }

    public Spec productReleases(Set<ProductRelease> productReleases) {
        this.productReleases = productReleases;
        return this;
    }

    public Spec addProductRelease(ProductRelease productRelease) {
        this.productReleases.add(productRelease);
        productRelease.getSpecs().add(this);
        return this;
    }

    public Spec removeProductRelease(ProductRelease productRelease) {
        this.productReleases.remove(productRelease);
        productRelease.getSpecs().remove(this);
        return this;
    }

    public void setProductReleases(Set<ProductRelease> productReleases) {
        this.productReleases = productReleases;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spec)) {
            return false;
        }
        return id != null && id.equals(((Spec) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spec{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", version='" + getVersion() + "'" +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", openApi='" + getOpenApi() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", checksum='" + getChecksum() + "'" +
            ", filename='" + getFilename() + "'" +
            ", valid='" + isValid() + "'" +
            ", order=" + getOrder() +
            ", parseError='" + getParseError() + "'" +
            ", externalDocs='" + getExternalDocs() + "'" +
            ", hide='" + isHide() + "'" +
            ", grade='" + getGrade() + "'" +
            ", backwardsCompatible='" + isBackwardsCompatible() + "'" +
            ", changed='" + isChanged() + "'" +
            ", sourcePath='" + getSourcePath() + "'" +
            ", sourceName='" + getSourceName() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", sourceCreatedBy='" + getSourceCreatedBy() + "'" +
            ", sourceCreatedOn='" + getSourceCreatedOn() + "'" +
            ", sourceLastModifiedOn='" + getSourceLastModifiedOn() + "'" +
            ", sourceLastModifiedBy='" + getSourceLastModifiedBy() + "'" +
            "}";
    }
}