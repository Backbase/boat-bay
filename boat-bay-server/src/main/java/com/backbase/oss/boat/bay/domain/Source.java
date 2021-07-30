package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Source.
 */
@Entity
@Table(name = "source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Source implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SourceType type;

    @NotNull
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "filter_artifacts_name")
    private String filterArtifactsName;

    @Column(name = "filter_artifacts_created_since")
    private LocalDate filterArtifactsCreatedSince;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "run_on_startup")
    private Boolean runOnStartup;

    @Column(name = "spec_filter_sp_el")
    private String specFilterSpEL;

    @Column(name = "capability_key_sp_el")
    private String capabilityKeySpEL;

    @Column(name = "capability_name_sp_el")
    private String capabilityNameSpEL;

    @Column(name = "service_key_sp_el")
    private String serviceKeySpEL;

    @Column(name = "service_name_sp_el")
    private String serviceNameSpEL;

    @Column(name = "spec_key_sp_el")
    private String specKeySpEL;

    @Column(name = "version_sp_el")
    private String versionSpEL;

    @Column(name = "product_release_name_sp_el")
    private String productReleaseNameSpEL;

    @Column(name = "product_release_version_sp_el")
    private String productReleaseVersionSpEL;

    @Column(name = "product_release_key_sp_el")
    private String productReleaseKeySpEL;

    @Column(name = "item_limit")
    private Integer itemLimit;

    @Column(name = "overwrite_changes")
    private Boolean overwriteChanges;

    @Lob
    @Column(name = "options")
    private String options;

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "source" }, allowSetters = true)
    private Set<SourcePath> sourcePaths = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "lintRules", "zallyConfig" }, allowSetters = true)
    private Portal portal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productReleases", "capabilities", "portal" }, allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "serviceDefinitions", "product" }, allowSetters = true)
    private Capability capability;

    @ManyToOne
    @JsonIgnoreProperties(value = { "specs", "capability" }, allowSetters = true)
    private ServiceDefinition serviceDefinition;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Source id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Source name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public Source key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SourceType getType() {
        return this.type;
    }

    public Source type(SourceType type) {
        this.type = type;
        return this;
    }

    public void setType(SourceType type) {
        this.type = type;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public Source baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Source active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFilterArtifactsName() {
        return this.filterArtifactsName;
    }

    public Source filterArtifactsName(String filterArtifactsName) {
        this.filterArtifactsName = filterArtifactsName;
        return this;
    }

    public void setFilterArtifactsName(String filterArtifactsName) {
        this.filterArtifactsName = filterArtifactsName;
    }

    public LocalDate getFilterArtifactsCreatedSince() {
        return this.filterArtifactsCreatedSince;
    }

    public Source filterArtifactsCreatedSince(LocalDate filterArtifactsCreatedSince) {
        this.filterArtifactsCreatedSince = filterArtifactsCreatedSince;
        return this;
    }

    public void setFilterArtifactsCreatedSince(LocalDate filterArtifactsCreatedSince) {
        this.filterArtifactsCreatedSince = filterArtifactsCreatedSince;
    }

    public String getUsername() {
        return this.username;
    }

    public Source username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Source password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public Source cronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Boolean getRunOnStartup() {
        return this.runOnStartup;
    }

    public Source runOnStartup(Boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
        return this;
    }

    public void setRunOnStartup(Boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
    }

    public String getSpecFilterSpEL() {
        return this.specFilterSpEL;
    }

    public Source specFilterSpEL(String specFilterSpEL) {
        this.specFilterSpEL = specFilterSpEL;
        return this;
    }

    public void setSpecFilterSpEL(String specFilterSpEL) {
        this.specFilterSpEL = specFilterSpEL;
    }

    public String getCapabilityKeySpEL() {
        return this.capabilityKeySpEL;
    }

    public Source capabilityKeySpEL(String capabilityKeySpEL) {
        this.capabilityKeySpEL = capabilityKeySpEL;
        return this;
    }

    public void setCapabilityKeySpEL(String capabilityKeySpEL) {
        this.capabilityKeySpEL = capabilityKeySpEL;
    }

    public String getCapabilityNameSpEL() {
        return this.capabilityNameSpEL;
    }

    public Source capabilityNameSpEL(String capabilityNameSpEL) {
        this.capabilityNameSpEL = capabilityNameSpEL;
        return this;
    }

    public void setCapabilityNameSpEL(String capabilityNameSpEL) {
        this.capabilityNameSpEL = capabilityNameSpEL;
    }

    public String getServiceKeySpEL() {
        return this.serviceKeySpEL;
    }

    public Source serviceKeySpEL(String serviceKeySpEL) {
        this.serviceKeySpEL = serviceKeySpEL;
        return this;
    }

    public void setServiceKeySpEL(String serviceKeySpEL) {
        this.serviceKeySpEL = serviceKeySpEL;
    }

    public String getServiceNameSpEL() {
        return this.serviceNameSpEL;
    }

    public Source serviceNameSpEL(String serviceNameSpEL) {
        this.serviceNameSpEL = serviceNameSpEL;
        return this;
    }

    public void setServiceNameSpEL(String serviceNameSpEL) {
        this.serviceNameSpEL = serviceNameSpEL;
    }

    public String getSpecKeySpEL() {
        return this.specKeySpEL;
    }

    public Source specKeySpEL(String specKeySpEL) {
        this.specKeySpEL = specKeySpEL;
        return this;
    }

    public void setSpecKeySpEL(String specKeySpEL) {
        this.specKeySpEL = specKeySpEL;
    }

    public String getVersionSpEL() {
        return this.versionSpEL;
    }

    public Source versionSpEL(String versionSpEL) {
        this.versionSpEL = versionSpEL;
        return this;
    }

    public void setVersionSpEL(String versionSpEL) {
        this.versionSpEL = versionSpEL;
    }

    public String getProductReleaseNameSpEL() {
        return this.productReleaseNameSpEL;
    }

    public Source productReleaseNameSpEL(String productReleaseNameSpEL) {
        this.productReleaseNameSpEL = productReleaseNameSpEL;
        return this;
    }

    public void setProductReleaseNameSpEL(String productReleaseNameSpEL) {
        this.productReleaseNameSpEL = productReleaseNameSpEL;
    }

    public String getProductReleaseVersionSpEL() {
        return this.productReleaseVersionSpEL;
    }

    public Source productReleaseVersionSpEL(String productReleaseVersionSpEL) {
        this.productReleaseVersionSpEL = productReleaseVersionSpEL;
        return this;
    }

    public void setProductReleaseVersionSpEL(String productReleaseVersionSpEL) {
        this.productReleaseVersionSpEL = productReleaseVersionSpEL;
    }

    public String getProductReleaseKeySpEL() {
        return this.productReleaseKeySpEL;
    }

    public Source productReleaseKeySpEL(String productReleaseKeySpEL) {
        this.productReleaseKeySpEL = productReleaseKeySpEL;
        return this;
    }

    public void setProductReleaseKeySpEL(String productReleaseKeySpEL) {
        this.productReleaseKeySpEL = productReleaseKeySpEL;
    }

    public Integer getItemLimit() {
        return this.itemLimit;
    }

    public Source itemLimit(Integer itemLimit) {
        this.itemLimit = itemLimit;
        return this;
    }

    public void setItemLimit(Integer itemLimit) {
        this.itemLimit = itemLimit;
    }

    public Boolean getOverwriteChanges() {
        return this.overwriteChanges;
    }

    public Source overwriteChanges(Boolean overwriteChanges) {
        this.overwriteChanges = overwriteChanges;
        return this;
    }

    public void setOverwriteChanges(Boolean overwriteChanges) {
        this.overwriteChanges = overwriteChanges;
    }

    public String getOptions() {
        return this.options;
    }

    public Source options(String options) {
        this.options = options;
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Set<SourcePath> getSourcePaths() {
        return this.sourcePaths;
    }

    public Source sourcePaths(Set<SourcePath> sourcePaths) {
        this.setSourcePaths(sourcePaths);
        return this;
    }

    public Source addSourcePath(SourcePath sourcePath) {
        this.sourcePaths.add(sourcePath);
        sourcePath.setSource(this);
        return this;
    }

    public Source removeSourcePath(SourcePath sourcePath) {
        this.sourcePaths.remove(sourcePath);
        sourcePath.setSource(null);
        return this;
    }

    public void setSourcePaths(Set<SourcePath> sourcePaths) {
        if (this.sourcePaths != null) {
            this.sourcePaths.forEach(i -> i.setSource(null));
        }
        if (sourcePaths != null) {
            sourcePaths.forEach(i -> i.setSource(this));
        }
        this.sourcePaths = sourcePaths;
    }

    public Portal getPortal() {
        return this.portal;
    }

    public Source portal(Portal portal) {
        this.setPortal(portal);
        return this;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Product getProduct() {
        return this.product;
    }

    public Source product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Capability getCapability() {
        return this.capability;
    }

    public Source capability(Capability capability) {
        this.setCapability(capability);
        return this;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public ServiceDefinition getServiceDefinition() {
        return this.serviceDefinition;
    }

    public Source serviceDefinition(ServiceDefinition serviceDefinition) {
        this.setServiceDefinition(serviceDefinition);
        return this;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        return id != null && id.equals(((Source) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Source{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", key='" + getKey() + "'" +
            ", type='" + getType() + "'" +
            ", baseUrl='" + getBaseUrl() + "'" +
            ", active='" + getActive() + "'" +
            ", filterArtifactsName='" + getFilterArtifactsName() + "'" +
            ", filterArtifactsCreatedSince='" + getFilterArtifactsCreatedSince() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", cronExpression='" + getCronExpression() + "'" +
            ", runOnStartup='" + getRunOnStartup() + "'" +
            ", specFilterSpEL='" + getSpecFilterSpEL() + "'" +
            ", capabilityKeySpEL='" + getCapabilityKeySpEL() + "'" +
            ", capabilityNameSpEL='" + getCapabilityNameSpEL() + "'" +
            ", serviceKeySpEL='" + getServiceKeySpEL() + "'" +
            ", serviceNameSpEL='" + getServiceNameSpEL() + "'" +
            ", specKeySpEL='" + getSpecKeySpEL() + "'" +
            ", versionSpEL='" + getVersionSpEL() + "'" +
            ", productReleaseNameSpEL='" + getProductReleaseNameSpEL() + "'" +
            ", productReleaseVersionSpEL='" + getProductReleaseVersionSpEL() + "'" +
            ", productReleaseKeySpEL='" + getProductReleaseKeySpEL() + "'" +
            ", itemLimit=" + getItemLimit() +
            ", overwriteChanges='" + getOverwriteChanges() + "'" +
            ", options='" + getOptions() + "'" +
            "}";
    }
}
