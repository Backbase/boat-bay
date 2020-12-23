package com.backbase.oss.boat.bay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "title")
    private String title;

    @Column(name = "open_api_url")
    private String openApiUrl;

    @Column(name = "boat_doc_url")
    private String boatDocUrl;

    @Column(name = "open_api")
    private String openApi;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @OneToOne
    @JoinColumn(unique = true)
    private LintReport lintReport;

    @ManyToOne
    @JsonIgnoreProperties(value = "specs", allowSetters = true)
    private Service service;

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

    public String getOpenApiUrl() {
        return openApiUrl;
    }

    public Spec openApiUrl(String openApiUrl) {
        this.openApiUrl = openApiUrl;
        return this;
    }

    public void setOpenApiUrl(String openApiUrl) {
        this.openApiUrl = openApiUrl;
    }

    public String getBoatDocUrl() {
        return boatDocUrl;
    }

    public Spec boatDocUrl(String boatDocUrl) {
        this.boatDocUrl = boatDocUrl;
        return this;
    }

    public void setBoatDocUrl(String boatDocUrl) {
        this.boatDocUrl = boatDocUrl;
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

    public Service getService() {
        return service;
    }

    public Spec service(Service service) {
        this.service = service;
        return this;
    }

    public void setService(Service service) {
        this.service = service;
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
            ", title='" + getTitle() + "'" +
            ", openApiUrl='" + getOpenApiUrl() + "'" +
            ", boatDocUrl='" + getBoatDocUrl() + "'" +
            ", openApi='" + getOpenApi() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
