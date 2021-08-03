package com.backbase.oss.boat.bay.web.views.dashboard.mapper;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.model.*;

import com.fasterxml.jackson.core.JsonPointer;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import kotlin.ranges.IntRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;
import org.zalando.zally.rule.api.RuleSet;

@Mapper(componentModel = "spring")
public interface BoatDashboardMapper {

    @Mapping(target = "numberOfServices", ignore = true)
    @Mapping(target = "numberOfCapabilities", ignore = true)
    @Mapping(target = "portalId", source = "portal.id")
    @Mapping(target = "portalKey", source = "portal.key")
    @Mapping(target = "portalName", source = "portal.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productKey", source = "product.key")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productDescription", source = "product.content")
    @Mapping(target = "lastLintReport", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    BoatPortalDashboard mapPortal(Portal portal, Product product);

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    static <T, K, U> Collector<T, ?, Map<K, U>> toLinkedMap(
        Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(
            keyMapper,
            valueMapper,
            (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            },
            LinkedHashMap::new
        );
    }


    default List<String> mapCapabilities(Set<Capability> capabilities) {
        return capabilities.stream()
            .map(Capability::getName)
            .collect(Collectors.toList());
    }

    @Mapping(target = "portalName", source = "portal.name")
    @Mapping(target = "portalKey", source = "portal.key")
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "lastLintReport", ignore = true)
    BoatProduct mapBoatProduct(Product product);

    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "lastLintReport", ignore = true)
    BoatCapability mapBoatCapability(Capability capability);

    default LocalDateTime map(Instant value) {
        if (value == null) {
            return null;
        }
        return LocalDateTime.ofInstant(value, ZoneId.systemDefault());
    }

    BoatPortal mapBoatPortal(Portal portal);

    @Mapping(target = "statistics", ignore = true)
    BoatService mapBoatService(ServiceDefinition serviceDefinition);

    @Mapping(target = "backwardsCompatible", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "openApi", ignore = true)
    BoatSpec mapBoatSpec(Spec spec);

    @Mapping(target = "version", source = "spec.version")
    @Mapping(target = "openApi", source = "spec.openApi")
    BoatLintReport mapReport(LintReport specReport);

    @Mapping(target = "version", source = "spec.version")
    @Mapping(target = "openApi", ignore = true)
    @Mapping(target = "violations", ignore = true)
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

    default JsonPointer map(String value) {
        return JsonPointer.valueOf(value);
    }


    @Mapping(target = "url", source = "externalUrl")
    BoatLintRule mapPortalLintRule(LintRule rule);


    @Mapping(target = "numberOfOccurrences", ignore = true)
    BoatTag mapTag(Tag tag);

    default int countSpecs(Tag tag) {
        return tag.getSpecs().size();
    }

    BoatProductRelease mapBoatProductRelease(ProductRelease productRelease);

    default URI mapUrl(String value) {
        if(StringUtils.hasText(value)) {
            return URI.create(value);
        } else {
            return null;
        }
    };
}
