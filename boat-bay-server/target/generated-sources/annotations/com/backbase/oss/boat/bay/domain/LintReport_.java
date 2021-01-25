package com.backbase.oss.boat.bay.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LintReport.class)
public abstract class LintReport_ {

	public static volatile SingularAttribute<LintReport, ZonedDateTime> lintedOn;
	public static volatile SingularAttribute<LintReport, String> grade;
	public static volatile SetAttribute<LintReport, LintRuleViolation> violations;
	public static volatile SingularAttribute<LintReport, String> name;
	public static volatile SingularAttribute<LintReport, Long> id;
	public static volatile SingularAttribute<LintReport, Boolean> passed;
	public static volatile SingularAttribute<LintReport, Spec> spec;

	public static final String LINTED_ON = "lintedOn";
	public static final String GRADE = "grade";
	public static final String VIOLATIONS = "violations";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String PASSED = "passed";
	public static final String SPEC = "spec";

}

