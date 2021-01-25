package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Boolean> hide;
	public static volatile SingularAttribute<Product, String> jiraProjectId;
	public static volatile SetAttribute<Product, Capability> capabilities;
	public static volatile SingularAttribute<Product, String> createdBy;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SetAttribute<Product, ProductRelease> productReleases;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, Portal> portal;
	public static volatile SingularAttribute<Product, ZonedDateTime> createdOn;
	public static volatile SingularAttribute<Product, String> key;
	public static volatile SingularAttribute<Product, String> content;
	public static volatile SingularAttribute<Product, Integer> order;

	public static final String HIDE = "hide";
	public static final String JIRA_PROJECT_ID = "jiraProjectId";
	public static final String CAPABILITIES = "capabilities";
	public static final String CREATED_BY = "createdBy";
	public static final String NAME = "name";
	public static final String PRODUCT_RELEASES = "productReleases";
	public static final String ID = "id";
	public static final String PORTAL = "portal";
	public static final String CREATED_ON = "createdOn";
	public static final String KEY = "key";
	public static final String CONTENT = "content";
	public static final String ORDER = "order";

}

