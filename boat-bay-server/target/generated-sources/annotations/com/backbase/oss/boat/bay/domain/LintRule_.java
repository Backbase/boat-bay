package com.backbase.oss.boat.bay.domain;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LintRule.class)
public abstract class LintRule_ {

	public static volatile SingularAttribute<LintRule, String> summary;
	public static volatile SingularAttribute<LintRule, Severity> severity;
	public static volatile SingularAttribute<LintRule, String> externalUrl;
	public static volatile SingularAttribute<LintRule, String> ruleSet;
	public static volatile SingularAttribute<LintRule, String> description;
	public static volatile SingularAttribute<LintRule, Long> id;
	public static volatile SingularAttribute<LintRule, String> ruleId;
	public static volatile SingularAttribute<LintRule, String> title;
	public static volatile SingularAttribute<LintRule, Portal> portal;
	public static volatile SingularAttribute<LintRule, Boolean> enabled;

	public static final String SUMMARY = "summary";
	public static final String SEVERITY = "severity";
	public static final String EXTERNAL_URL = "externalUrl";
	public static final String RULE_SET = "ruleSet";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String RULE_ID = "ruleId";
	public static final String TITLE = "title";
	public static final String PORTAL = "portal";
	public static final String ENABLED = "enabled";

}

