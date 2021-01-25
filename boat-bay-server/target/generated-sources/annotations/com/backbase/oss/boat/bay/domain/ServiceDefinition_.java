package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ServiceDefinition.class)
public abstract class ServiceDefinition_ {

	public static volatile SingularAttribute<ServiceDefinition, String> color;
	public static volatile SingularAttribute<ServiceDefinition, String> icon;
	public static volatile SingularAttribute<ServiceDefinition, String> description;
	public static volatile SingularAttribute<ServiceDefinition, ZonedDateTime> createdOn;
	public static volatile SetAttribute<ServiceDefinition, Spec> specs;
	public static volatile SingularAttribute<ServiceDefinition, Boolean> hide;
	public static volatile SingularAttribute<ServiceDefinition, Capability> capability;
	public static volatile SingularAttribute<ServiceDefinition, String> subTitle;
	public static volatile SingularAttribute<ServiceDefinition, String> createdBy;
	public static volatile SingularAttribute<ServiceDefinition, String> name;
	public static volatile SingularAttribute<ServiceDefinition, Long> id;
	public static volatile SingularAttribute<ServiceDefinition, String> key;
	public static volatile SingularAttribute<ServiceDefinition, Integer> order;

	public static final String COLOR = "color";
	public static final String ICON = "icon";
	public static final String DESCRIPTION = "description";
	public static final String CREATED_ON = "createdOn";
	public static final String SPECS = "specs";
	public static final String HIDE = "hide";
	public static final String CAPABILITY = "capability";
	public static final String SUB_TITLE = "subTitle";
	public static final String CREATED_BY = "createdBy";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String KEY = "key";
	public static final String ORDER = "order";

}

