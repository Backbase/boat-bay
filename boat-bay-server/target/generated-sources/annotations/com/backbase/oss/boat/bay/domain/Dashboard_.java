package com.backbase.oss.boat.bay.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Dashboard.class)
public abstract class Dashboard_ {

	public static volatile SingularAttribute<Dashboard, String> subTitle;
	public static volatile SingularAttribute<Dashboard, String> name;
	public static volatile SingularAttribute<Dashboard, Portal> defaultPortal;
	public static volatile SingularAttribute<Dashboard, Long> id;
	public static volatile SingularAttribute<Dashboard, String> title;
	public static volatile SingularAttribute<Dashboard, String> content;
	public static volatile SingularAttribute<Dashboard, String> navTitle;

	public static final String SUB_TITLE = "subTitle";
	public static final String NAME = "name";
	public static final String DEFAULT_PORTAL = "defaultPortal";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String NAV_TITLE = "navTitle";

}

