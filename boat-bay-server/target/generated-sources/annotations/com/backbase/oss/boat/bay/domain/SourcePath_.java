package com.backbase.oss.boat.bay.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SourcePath.class)
public abstract class SourcePath_ {

	public static volatile SingularAttribute<SourcePath, String> name;
	public static volatile SingularAttribute<SourcePath, Long> id;
	public static volatile SingularAttribute<SourcePath, Source> source;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String SOURCE = "source";

}

