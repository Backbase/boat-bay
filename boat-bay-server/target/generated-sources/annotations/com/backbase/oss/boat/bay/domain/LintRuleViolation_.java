package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LintRuleViolation.class)
public abstract class LintRuleViolation_ {

	public static volatile SingularAttribute<LintRuleViolation, Severity> severity;
	public static volatile SingularAttribute<LintRuleViolation, String> jsonPointer;
	public static volatile SingularAttribute<LintRuleViolation, LintRule> lintRule;
	public static volatile SingularAttribute<LintRuleViolation, LintReport> lintReport;
	public static volatile SingularAttribute<LintRuleViolation, Integer> lineStart;
	public static volatile SingularAttribute<LintRuleViolation, String> name;
	public static volatile SingularAttribute<LintRuleViolation, String> description;
	public static volatile SingularAttribute<LintRuleViolation, Long> id;
	public static volatile SingularAttribute<LintRuleViolation, Integer> lineEnd;
	public static volatile SingularAttribute<LintRuleViolation, String> url;

	public static final String SEVERITY = "severity";
	public static final String JSON_POINTER = "jsonPointer";
	public static final String LINT_RULE = "lintRule";
	public static final String LINT_REPORT = "lintReport";
	public static final String LINE_START = "lineStart";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String LINE_END = "lineEnd";
	public static final String URL = "url";

}

