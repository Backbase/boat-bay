package com.backbase.oss.boat.bay.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public abstract class Tag_ {

	public static volatile SetAttribute<Tag, Spec> specs;
	public static volatile SingularAttribute<Tag, Boolean> hide;
	public static volatile SingularAttribute<Tag, String> color;
	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, String> description;
	public static volatile SingularAttribute<Tag, Long> id;

	public static final String SPECS = "specs";
	public static final String HIDE = "hide";
	public static final String COLOR = "color";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";

}

