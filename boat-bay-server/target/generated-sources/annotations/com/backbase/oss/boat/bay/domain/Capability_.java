package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Capability.class)
public abstract class Capability_ {

	public static volatile SingularAttribute<Capability, Boolean> hide;
	public static volatile SingularAttribute<Capability, Product> product;
	public static volatile SingularAttribute<Capability, String> subTitle;
	public static volatile SingularAttribute<Capability, String> createdBy;
	public static volatile SingularAttribute<Capability, String> name;
	public static volatile SingularAttribute<Capability, Long> id;
	public static volatile SingularAttribute<Capability, ZonedDateTime> createdOn;
	public static volatile SingularAttribute<Capability, String> key;
	public static volatile SingularAttribute<Capability, String> content;
	public static volatile SetAttribute<Capability, ServiceDefinition> serviceDefinitions;
	public static volatile SingularAttribute<Capability, Integer> order;

	public static final String HIDE = "hide";
	public static final String PRODUCT = "product";
	public static final String SUB_TITLE = "subTitle";
	public static final String CREATED_BY = "createdBy";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String CREATED_ON = "createdOn";
	public static final String KEY = "key";
	public static final String CONTENT = "content";
	public static final String SERVICE_DEFINITIONS = "serviceDefinitions";
	public static final String ORDER = "order";

}

