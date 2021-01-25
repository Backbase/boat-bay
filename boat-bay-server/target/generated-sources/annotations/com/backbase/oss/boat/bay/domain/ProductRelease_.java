package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductRelease.class)
public abstract class ProductRelease_ {

	public static volatile SetAttribute<ProductRelease, Spec> specs;
	public static volatile SingularAttribute<ProductRelease, Boolean> hide;
	public static volatile SingularAttribute<ProductRelease, Product> product;
	public static volatile SingularAttribute<ProductRelease, ZonedDateTime> releaseDate;
	public static volatile SingularAttribute<ProductRelease, String> name;
	public static volatile SingularAttribute<ProductRelease, Long> id;
	public static volatile SingularAttribute<ProductRelease, String> version;
	public static volatile SingularAttribute<ProductRelease, String> key;

	public static final String SPECS = "specs";
	public static final String HIDE = "hide";
	public static final String PRODUCT = "product";
	public static final String RELEASE_DATE = "releaseDate";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String KEY = "key";

}

