package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.SourceType;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Source.class)
public abstract class Source_ {

	public static volatile SingularAttribute<Source, String> specKeySpEL;
	public static volatile SingularAttribute<Source, String> serviceNameSpEL;
	public static volatile SetAttribute<Source, SourcePath> sourcePaths;
	public static volatile SingularAttribute<Source, SourceType> type;
	public static volatile SingularAttribute<Source, String> specFilterSpEL;
	public static volatile SingularAttribute<Source, String> filterArtifactsName;
	public static volatile SingularAttribute<Source, String> productReleaseNameSpEL;
	public static volatile SingularAttribute<Source, String> productReleaseVersionSpEL;
	public static volatile SingularAttribute<Source, String> password;
	public static volatile SingularAttribute<Source, Capability> capability;
	public static volatile SingularAttribute<Source, LocalDate> filterArtifactsCreatedSince;
	public static volatile SingularAttribute<Source, Long> id;
	public static volatile SingularAttribute<Source, Boolean> overwriteChanges;
	public static volatile SingularAttribute<Source, Portal> portal;
	public static volatile SingularAttribute<Source, Boolean> runOnStartup;
	public static volatile SingularAttribute<Source, String> capabilityKeySpEL;
	public static volatile SingularAttribute<Source, Product> product;
	public static volatile SingularAttribute<Source, Integer> itemLimit;
	public static volatile SingularAttribute<Source, Boolean> active;
	public static volatile SingularAttribute<Source, String> capabilityNameSpEL;
	public static volatile SingularAttribute<Source, String> serviceKeySpEL;
	public static volatile SingularAttribute<Source, String> versionSpEL;
	public static volatile SingularAttribute<Source, String> cronExpression;
	public static volatile SingularAttribute<Source, String> baseUrl;
	public static volatile SingularAttribute<Source, String> productReleaseKeySpEL;
	public static volatile SingularAttribute<Source, String> name;
	public static volatile SingularAttribute<Source, ServiceDefinition> serviceDefinition;
	public static volatile SingularAttribute<Source, String> username;

	public static final String SPEC_KEY_SP_EL = "specKeySpEL";
	public static final String SERVICE_NAME_SP_EL = "serviceNameSpEL";
	public static final String SOURCE_PATHS = "sourcePaths";
	public static final String TYPE = "type";
	public static final String SPEC_FILTER_SP_EL = "specFilterSpEL";
	public static final String FILTER_ARTIFACTS_NAME = "filterArtifactsName";
	public static final String PRODUCT_RELEASE_NAME_SP_EL = "productReleaseNameSpEL";
	public static final String PRODUCT_RELEASE_VERSION_SP_EL = "productReleaseVersionSpEL";
	public static final String PASSWORD = "password";
	public static final String CAPABILITY = "capability";
	public static final String FILTER_ARTIFACTS_CREATED_SINCE = "filterArtifactsCreatedSince";
	public static final String ID = "id";
	public static final String OVERWRITE_CHANGES = "overwriteChanges";
	public static final String PORTAL = "portal";
	public static final String RUN_ON_STARTUP = "runOnStartup";
	public static final String CAPABILITY_KEY_SP_EL = "capabilityKeySpEL";
	public static final String PRODUCT = "product";
	public static final String ITEM_LIMIT = "itemLimit";
	public static final String ACTIVE = "active";
	public static final String CAPABILITY_NAME_SP_EL = "capabilityNameSpEL";
	public static final String SERVICE_KEY_SP_EL = "serviceKeySpEL";
	public static final String VERSION_SP_EL = "versionSpEL";
	public static final String CRON_EXPRESSION = "cronExpression";
	public static final String BASE_URL = "baseUrl";
	public static final String PRODUCT_RELEASE_KEY_SP_EL = "productReleaseKeySpEL";
	public static final String NAME = "name";
	public static final String SERVICE_DEFINITION = "serviceDefinition";
	public static final String USERNAME = "username";

}

