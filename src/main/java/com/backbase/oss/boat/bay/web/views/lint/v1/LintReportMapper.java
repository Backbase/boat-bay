package com.backbase.oss.boat.bay.web.views.lint.v1;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.quay.model.BoatLintReport;
import com.backbase.oss.boat.quay.model.BoatViolation;
import com.fasterxml.jackson.core.JsonPointer;
import kotlin.ranges.IntRange;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.zalando.zally.rule.api.RuleSet;

@Mapper(componentModel = "spring")
public interface LintReportMapper {

    @Mapping(target = "availableRules", ignore = true)
    @Mapping(target = "version", source = "spec.version")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "openApi", source = "spec.openApi")
    @Mapping(target = "filePath", source = "spec.filename")
    BoatLintReport mapReport(LintReport specReport);

    @Mapping(target = "availableRules", ignore = true)
    @Mapping(target = "version", source = "spec.version")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "filePath", source = "spec.filename")
    @Mapping(target = "openApi", ignore = true)
    @Mapping(target = "violations",ignore = true)
    BoatLintReport mapReportWithoutViolations(LintReport lintReport);

    @Mapping(target = "lines", expression = "java(mapRange(lintRuleViolation))")
    @Mapping(target = "rule", source = "lintRule")
    @Mapping(target = "pointer", source = "jsonPointer")
    BoatViolation mapViolation(LintRuleViolation lintRuleViolation);

    default IntRange mapRange(LintRuleViolation violation) {
        return new IntRange(violation.getLineStart(), violation.getLineEnd());
    }

    default String mapRuleSet(RuleSet value) {
        return value.getId();
    }

    default String map(LintRuleSet value) {
        return value.getRuleSetId();
    }

    default JsonPointer map(String value) {
        return JsonPointer.valueOf(value);
    }



}
