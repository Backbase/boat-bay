package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

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

    
    @Lob
    @Column(name = "open_api", nullable = false)
    private String openApi;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "checksum", nullable = false)
    private String checksum;

    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "source_path")
    private String sourcePath;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "source_created_by")
    private String sourceCreatedBy;

    @Column(name = "source_created_on")
    private Instant sourceCreatedOn;

    @Column(name = "source_last_modified_on")
    private Instant sourceLastModifiedOn;

    @Column(name = "source_last_modified_by")
    private String sourceLastModifiedBy;

    @OneToOne
    @JoinColumn(unique = true)
    private LintReport lintReport;

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
    private ServiceDefinition serviceDefinition;

    @ManyToOne
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Source source;

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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Spec createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
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

    public Instant getSourceCreatedOn() {
        return sourceCreatedOn;
    }

    public Spec sourceCreatedOn(Instant sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
        return this;
    }

    public void setSourceCreatedOn(Instant sourceCreatedOn) {
        this.sourceCreatedOn = sourceCreatedOn;
    }

    public Instant getSourceLastModifiedOn() {
        return sourceLastModifiedOn;
    }

    public Spec sourceLastModifiedOn(Instant sourceLastModifiedOn) {
        this.sourceLastModifiedOn = sourceLastModifiedOn;
        return this;
    }

    public void setSourceLastModifiedOn(Instant sourceLastModifiedOn) {
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
            ", openApi='" + getOpenApi() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", checksum='" + getChecksum() + "'" +
            ", filename='" + getFilename() + "'" +
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
