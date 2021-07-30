package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.Changes;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "changes")
    private Changes changes;

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

    @Column(name = "mvn_group_id")
    private String mvnGroupId;

    @Column(name = "mvn_artifact_id")
    private String mvnArtifactId;

    @Column(name = "mvn_version")
    private String mvnVersion;

    @Column(name = "mvn_classifier")
    private String mvnClassifier;

    @Column(name = "mvn_extension")
    private String mvnExtension;

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
    @OneToOne
    @JoinColumn(unique = true)
    private Spec previousSpec;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "lintRules", "zallyConfig" }, allowSetters = true)
    private Portal portal;

    @ManyToOne
    @JsonIgnoreProperties(value = { "serviceDefinitions", "product" }, allowSetters = true)
    private Capability capability;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productReleases", "capabilities", "portal" }, allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sourcePaths", "portal", "product", "capability", "serviceDefinition" }, allowSetters = true)
    private Source source;

    @ManyToOne
    private SpecType specType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_spec__tag", joinColumns = @JoinColumn(name = "spec_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties(value = { "specs" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnoreProperties(value = { "spec", "violations" }, allowSetters = true)
    @OneToOne(mappedBy = "spec")
    private LintReport lintReport;

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
    @OneToOne(mappedBy = "previousSpec")
    private Spec successor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "specs", "capability" }, allowSetters = true)
    private ServiceDefinition serviceDefinition;

    @ManyToMany(mappedBy = "specs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "specs", "product" }, allowSetters = true)
    private Set<ProductRelease> productReleases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Spec id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public Spec key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public Spec name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public Spec version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public Spec title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public Spec icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenApi() {
        return this.openApi;
    }

    public Spec openApi(String openApi) {
        this.openApi = openApi;
        return this;
    }

    public void setOpenApi(String openApi) {
        this.openApi = openApi;
    }

    public String getDescription() {
        return this.description;
    }

    public Spec description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedOn() {
        return this.createdOn;
    }

    public Spec createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Spec createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public Spec checksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getFilename() {
        return this.filename;
    }

    public Spec filename(String filename) {
        this.filename = filename;
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getValid() {
        return this.valid;
    }

    public Spec valid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Spec order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getParseError() {
        return this.parseError;
    }

    public Spec parseError(String parseError) {
        this.parseError = parseError;
        return this;
    }

    public void setParseError(String parseError) {
        this.parseError = parseError;
    }

    public String getExternalDocs() {
        return this.externalDocs;
    }

    public Spec externalDocs(String externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    public void setExternalDocs(String externalDocs) {
        this.externalDocs = externalDocs;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public Spec hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getGrade() {
        return this.grade;
    }

    public Spec grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Changes getChanges() {
        return this.changes;
    }

    public Spec changes(Changes changes) {
        this.changes = changes;
        return this;
    }

    public void setChanges(Changes changes) {
        this.changes = changes;
    }

    public String getSourcePath() {
        return this.sourcePath;
    }

    public Spec sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public Spec sourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public Spec sourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceCreatedBy() {
        return this.sourceCreatedBy;
    }

    public Spec sourceCreatedBy(String sourceCreatedBy) {
        this.sourceCreatedBy = sourceCreatedBy;
        return this;
    }

    public void setSourceCreatedBy(String sourceCreatedBy) {
        this.sourceCreatedBy = sourceCreatedBy;
    }

    public ZonedDateTime getSourceCreatedOn() {
        return this.sourceCreatedOn;
    }

    public Spec sourceCreatedOn(ZonedDateTime sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
        return this;
    }

    public void setSourceCreatedOn(ZonedDateTime sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
    }

    public ZonedDateTime getSourceLastModifiedOn() {
        return this.sourceLastModifiedOn;
    }

    public Spec sourceLastModifiedOn(ZonedDateTime sourceLastModifiedOn) {
        this.sourceLastModifiedOn = sourceLastModifiedOn;
        return this;
    }

    public void setSourceLastModifiedOn(ZonedDateTime sourceLastModifiedOn) {
        this.sourceLastModifiedOn = sourceLastModifiedOn;
    }

    public String getSourceLastModifiedBy() {
        return this.sourceLastModifiedBy;
    }

    public Spec sourceLastModifiedBy(String sourceLastModifiedBy) {
        this.sourceLastModifiedBy = sourceLastModifiedBy;
        return this;
    }

    public void setSourceLastModifiedBy(String sourceLastModifiedBy) {
        this.sourceLastModifiedBy = sourceLastModifiedBy;
    }

    public String getMvnGroupId() {
        return this.mvnGroupId;
    }

    public Spec mvnGroupId(String mvnGroupId) {
        this.mvnGroupId = mvnGroupId;
        return this;
    }

    public void setMvnGroupId(String mvnGroupId) {
        this.mvnGroupId = mvnGroupId;
    }

    public String getMvnArtifactId() {
        return this.mvnArtifactId;
    }

    public Spec mvnArtifactId(String mvnArtifactId) {
        this.mvnArtifactId = mvnArtifactId;
        return this;
    }

    public void setMvnArtifactId(String mvnArtifactId) {
        this.mvnArtifactId = mvnArtifactId;
    }

    public String getMvnVersion() {
        return this.mvnVersion;
    }

    public Spec mvnVersion(String mvnVersion) {
        this.mvnVersion = mvnVersion;
        return this;
    }

    public void setMvnVersion(String mvnVersion) {
        this.mvnVersion = mvnVersion;
    }

    public String getMvnClassifier() {
        return this.mvnClassifier;
    }

    public Spec mvnClassifier(String mvnClassifier) {
        this.mvnClassifier = mvnClassifier;
        return this;
    }

    public void setMvnClassifier(String mvnClassifier) {
        this.mvnClassifier = mvnClassifier;
    }

    public String getMvnExtension() {
        return this.mvnExtension;
    }

    public Spec mvnExtension(String mvnExtension) {
        this.mvnExtension = mvnExtension;
        return this;
    }

    public void setMvnExtension(String mvnExtension) {
        this.mvnExtension = mvnExtension;
    }

    public Spec getPreviousSpec() {
        return this.previousSpec;
    }

    public Spec previousSpec(Spec spec) {
        this.setPreviousSpec(spec);
        return this;
    }

    public void setPreviousSpec(Spec spec) {
        this.previousSpec = spec;
    }

    public Portal getPortal() {
        return this.portal;
    }

    public Spec portal(Portal portal) {
        this.setPortal(portal);
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Capability getCapability() {
        return this.capability;
    }

    public Spec capability(Capability capability) {
        this.setCapability(capability);
        return this;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public Product getProduct() {
        return this.product;
    }

    public Spec product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Source getSource() {
        return this.source;
    }

    public Spec source(Source source) {
        this.setSource(source);
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SpecType getSpecType() {
        return this.specType;
    }

    public Spec specType(SpecType specType) {
        this.setSpecType(specType);
        return this;
    }

    public void setSpecType(SpecType specType) {
        this.specType = specType;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public Spec tags(Set<Tag> tags) {
        this.setTags(tags);
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
        return this.lintReport;
    }

    public Spec lintReport(LintReport lintReport) {
        this.setLintReport(lintReport);
        return this;
    }

    public void setLintReport(LintReport lintReport) {
        if (this.lintReport != null) {
            this.lintReport.setSpec(null);
        }
        if (lintReport != null) {
            lintReport.setSpec(this);
        }
        this.lintReport = lintReport;
    }

    public Spec getSuccessor() {
        return this.successor;
    }

    public Spec successor(Spec spec) {
        this.setSuccessor(spec);
        return this;
    }

    public void setSuccessor(Spec spec) {
        if (this.successor != null) {
            this.successor.setPreviousSpec(null);
        }
        if (spec != null) {
            spec.setPreviousSpec(this);
        }
        this.successor = spec;
    }

    public ServiceDefinition getServiceDefinition() {
        return this.serviceDefinition;
    }

    public Spec serviceDefinition(ServiceDefinition serviceDefinition) {
        this.setServiceDefinition(serviceDefinition);
        return this;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    public Set<ProductRelease> getProductReleases() {
        return this.productReleases;
    }

    public Spec productReleases(Set<ProductRelease> productReleases) {
        this.setProductReleases(productReleases);
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
        if (this.productReleases != null) {
            this.productReleases.forEach(i -> i.removeSpec(this));
        }
        if (productReleases != null) {
            productReleases.forEach(i -> i.addSpec(this));
        }
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
            ", valid='" + getValid() + "'" +
            ", order=" + getOrder() +
            ", parseError='" + getParseError() + "'" +
            ", externalDocs='" + getExternalDocs() + "'" +
            ", hide='" + getHide() + "'" +
            ", grade='" + getGrade() + "'" +
            ", changes='" + getChanges() + "'" +
            ", sourcePath='" + getSourcePath() + "'" +
            ", sourceName='" + getSourceName() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", sourceCreatedBy='" + getSourceCreatedBy() + "'" +
            ", sourceCreatedOn='" + getSourceCreatedOn() + "'" +
            ", sourceLastModifiedOn='" + getSourceLastModifiedOn() + "'" +
            ", sourceLastModifiedBy='" + getSourceLastModifiedBy() + "'" +
            ", mvnGroupId='" + getMvnGroupId() + "'" +
            ", mvnArtifactId='" + getMvnArtifactId() + "'" +
            ", mvnVersion='" + getMvnVersion() + "'" +
            ", mvnClassifier='" + getMvnClassifier() + "'" +
            ", mvnExtension='" + getMvnExtension() + "'" +
            "}";
    }
}
