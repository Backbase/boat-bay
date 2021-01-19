package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.domain.LintRuleViolation;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.PortalLintRule;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintRule;
import com.backbase.oss.boat.bay.web.views.lint.BoatViolation;
import com.fasterxml.jackson.core.JsonPointer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
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
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.zalando.zally.rule.api.RuleSet;

@Mapper(componentModel = "spring")
public interface BoatDashboardMapper {

    @Mapping(target = "releases", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    BoatLegacyPortal mapPortal(Portal portal);

    BoatLegacyPortal.ProductDto mapProduct(Product product);

    @Mapping(target = "modules", source = "serviceDefinitions")
    BoatLegacyPortal.CapabilityDto mapCapability(Capability capability);

    BoatLegacyPortal.PortalVersionDto mapPortalVersion(Portal portal);

    @Mapping(target = "XIcon", source = "icon")
    @Mapping(target = "tags", ignore = true)
    BoatLegacyPortal.ModuleDto mapModule(ServiceDefinition serviceDefinition);

    @Mapping(target = "grade", source = "spec.lintReport.grade")
    @Mapping(target = "icon", source = "spec.specType.icon")
    BoatLegacyPortal.SpecDto mapSpec(Spec spec);

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


    default Map<String, BoatLegacyPortal.CapabilityDto> mapCapabilities(Portal portal, List<String> allEnabledTags) {
        return portal.getProducts().stream()
            .flatMap(p -> p.getCapabilities().stream())
            .map(capability -> {
                BoatLegacyPortal.CapabilityDto capabilityDto1 = mapCapability(capability);
                capabilityDto1.getModules().values().forEach(moduleDto -> moduleDto.setTags(moduleDto.getTags().stream().filter(allEnabledTags::contains).collect(Collectors.toSet())));
                return capabilityDto1;
            })
            .collect(Collectors.toMap(BoatLegacyPortal.CapabilityDto::getKey, capabilityDto -> capabilityDto));
    }

    default Map<String, BoatLegacyPortal.ModuleDto> mapModules(Set<ServiceDefinition> serviceDefinitions) {
        Set<String> tags = serviceDefinitions.stream().flatMap(sd -> sd.getSpecs().stream()).flatMap(spec -> spec.getTags().stream()).map(Tag::getName).collect(Collectors.toSet());

        return serviceDefinitions.stream()
            .map(serviceDefinition -> {
                BoatLegacyPortal.ModuleDto moduleDto = mapModule(serviceDefinition);
                moduleDto.setTags(tags);
                return moduleDto;
            })
            .collect(Collectors.toMap(BoatLegacyPortal.ModuleDto::getKey, moduleDto -> moduleDto));
    }


    default Map<Long, BoatLegacyPortal.SpecDto> map(Set<Spec> specs) {
        return specs.stream()
            .map(this::mapSpec)
            .collect(Collectors.toMap(BoatLegacyPortal.SpecDto::getId, specDto -> specDto));
    }


    default Map<String, Map<String, BoatLegacyPortal.ProductReleaseDto>> mapReleases(Portal portal) {
        Map<String, Map<String, BoatLegacyPortal.ProductReleaseDto>> result = new LinkedHashMap<>();
        portal.getProductReleases()
            .forEach(productRelease -> result.put(productRelease.getKey(), mapProductRelease(productRelease)));
        return result;

    }

    @NotNull
    private Map<String, BoatLegacyPortal.ProductReleaseDto> mapProductRelease(ProductRelease productRelease) {
        Map<String, BoatLegacyPortal.ProductReleaseDto> result = new LinkedHashMap<>();
        productRelease.getSpecs().stream().collect(Collectors.groupingBy(Spec::getProduct)).forEach((product, specs) -> {
            BoatLegacyPortal.ProductReleaseDto pr = new BoatLegacyPortal.ProductReleaseDto();
            pr.setKey(productRelease.getKey());
            pr.setTitle(productRelease.getName());
            pr.setServices(specs.stream().map(Spec::getServiceDefinition)
                .sorted(Comparator.comparing(ServiceDefinition::getName))
                .filter(distinctByKey(ServiceDefinition::getKey))
                .collect(toLinkedMap(ServiceDefinition::getKey, ServiceDefinition::getName)));
            pr.setSpecs(specs.stream().map(Spec::getId).collect(Collectors.toSet()));
            result.put(product.getKey(), pr);
        });
        return result;
    }

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

    default Map<String, BoatLegacyPortal.ProductDto> mapProducts(Set<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(Product::getKey, this::mapProduct));
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
        if(value == null) {
            return null;
        }
        return LocalDateTime.ofInstant(value, ZoneId.systemDefault());
    }

    BoatPortal mapBoatPortal(Portal portal);

    @Mapping(target = "statistics", ignore = true)
    BoatService mapBoatService(ServiceDefinition serviceDefinition);

    @Mapping(target = "statistics", ignore = true)
    BoatSpec mapBoatSpec(Spec spec);

    @Mapping(target = "version", source = "spec.version")
    @Mapping(target = "openApi", source = "spec.openApi")
    BoatLintReport mapReport(LintReport specReport);

    @Mapping(target = "version", source = "spec.version")
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

    BoatLintRule mapPortalLintRule(PortalLintRule portalLintRule);

}
