package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Portal.class)
public abstract class Portal_ {

	public static volatile SetAttribute<Portal, LintRule> lintRules;
	public static volatile SingularAttribute<Portal, Boolean> linted;
	public static volatile SingularAttribute<Portal, ZonedDateTime> createdOn;
	public static volatile SingularAttribute<Portal, String> logoUrl;
	public static volatile SingularAttribute<Portal, String> content;
	public static volatile SetAttribute<Portal, Product> products;
	public static volatile SingularAttribute<Portal, Boolean> hide;
	public static volatile SingularAttribute<Portal, String> logoLink;
	public static volatile SingularAttribute<Portal, String> subTitle;
	public static volatile SingularAttribute<Portal, String> createdBy;
	public static volatile SingularAttribute<Portal, String> name;
	public static volatile SingularAttribute<Portal, Long> id;
	public static volatile SingularAttribute<Portal, String> key;

	public static final String LINT_RULES = "lintRules";
	public static final String LINTED = "linted";
	public static final String CREATED_ON = "createdOn";
	public static final String LOGO_URL = "logoUrl";
	public static final String CONTENT = "content";
	public static final String PRODUCTS = "products";
	public static final String HIDE = "hide";
	public static final String LOGO_LINK = "logoLink";
	public static final String SUB_TITLE = "subTitle";
	public static final String CREATED_BY = "createdBy";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String KEY = "key";

}

