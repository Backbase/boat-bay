package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Spec.class)
public abstract class Spec_ {

	public static volatile SingularAttribute<Spec, SpecType> specType;
	public static volatile SingularAttribute<Spec, String> openApi;
	public static volatile SingularAttribute<Spec, String> sourceCreatedBy;
	public static volatile SingularAttribute<Spec, LintReport> lintReport;
	public static volatile SingularAttribute<Spec, String> sourceLastModifiedBy;
	public static volatile SingularAttribute<Spec, String> icon;
	public static volatile SingularAttribute<Spec, String> parseError;
	public static volatile SingularAttribute<Spec, String> description;
	public static volatile SingularAttribute<Spec, Source> source;
	public static volatile SingularAttribute<Spec, String> title;
	public static volatile SingularAttribute<Spec, ZonedDateTime> sourceCreatedOn;
	public static volatile SingularAttribute<Spec, ZonedDateTime> createdOn;
	public static volatile SingularAttribute<Spec, ZonedDateTime> sourceLastModifiedOn;
	public static volatile SingularAttribute<Spec, Boolean> valid;
	public static volatile SingularAttribute<Spec, String> sourceUrl;
	public static volatile SingularAttribute<Spec, Capability> capability;
	public static volatile SingularAttribute<Spec, String> checksum;
	public static volatile SingularAttribute<Spec, Long> id;
	public static volatile SingularAttribute<Spec, Portal> portal;
	public static volatile SingularAttribute<Spec, String> key;
	public static volatile SingularAttribute<Spec, String> sourcePath;
	public static volatile SingularAttribute<Spec, Integer> order;
	public static volatile SingularAttribute<Spec, Product> product;
	public static volatile SingularAttribute<Spec, Boolean> backwardsCompatible;
	public static volatile SingularAttribute<Spec, String> version;
	public static volatile SetAttribute<Spec, Tag> tags;
	public static volatile SingularAttribute<Spec, Boolean> hide;
	public static volatile SingularAttribute<Spec, String> filename;
	public static volatile SingularAttribute<Spec, String> createdBy;
	public static volatile SingularAttribute<Spec, String> grade;
	public static volatile SingularAttribute<Spec, String> name;
	public static volatile SetAttribute<Spec, ProductRelease> productReleases;
	public static volatile SingularAttribute<Spec, ServiceDefinition> serviceDefinition;
	public static volatile SingularAttribute<Spec, String> externalDocs;
	public static volatile SingularAttribute<Spec, String> sourceName;
	public static volatile SingularAttribute<Spec, Boolean> changed;

	public static final String SPEC_TYPE = "specType";
	public static final String OPEN_API = "openApi";
	public static final String SOURCE_CREATED_BY = "sourceCreatedBy";
	public static final String LINT_REPORT = "lintReport";
	public static final String SOURCE_LAST_MODIFIED_BY = "sourceLastModifiedBy";
	public static final String ICON = "icon";
	public static final String PARSE_ERROR = "parseError";
	public static final String DESCRIPTION = "description";
	public static final String SOURCE = "source";
	public static final String TITLE = "title";
	public static final String SOURCE_CREATED_ON = "sourceCreatedOn";
	public static final String CREATED_ON = "createdOn";
	public static final String SOURCE_LAST_MODIFIED_ON = "sourceLastModifiedOn";
	public static final String VALID = "valid";
	public static final String SOURCE_URL = "sourceUrl";
	public static final String CAPABILITY = "capability";
	public static final String CHECKSUM = "checksum";
	public static final String ID = "id";
	public static final String PORTAL = "portal";
	public static final String KEY = "key";
	public static final String SOURCE_PATH = "sourcePath";
	public static final String ORDER = "order";
	public static final String PRODUCT = "product";
	public static final String BACKWARDS_COMPATIBLE = "backwardsCompatible";
	public static final String VERSION = "version";
	public static final String TAGS = "tags";
	public static final String HIDE = "hide";
	public static final String FILENAME = "filename";
	public static final String CREATED_BY = "createdBy";
	public static final String GRADE = "grade";
	public static final String NAME = "name";
	public static final String PRODUCT_RELEASES = "productReleases";
	public static final String SERVICE_DEFINITION = "serviceDefinition";
	public static final String EXTERNAL_DOCS = "externalDocs";
	public static final String SOURCE_NAME = "sourceName";
	public static final String CHANGED = "changed";

}

