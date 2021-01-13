package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
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
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoatDashboardMapper {

    @Mapping(target = "releases", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    BoatLegacyPortalDto mapPortal(Portal portal);

    BoatLegacyPortalDto.ProductDto mapProduct(Product product);

    @Mapping(target = "modules", source = "serviceDefinitions")
    BoatLegacyPortalDto.CapabilityDto mapCapability(Capability capability);

    BoatLegacyPortalDto.PortalVersionDto mapPortalVersion(Portal portal);

    @Mapping(target = "XIcon", source = "icon")
    @Mapping(target = "tags", ignore = true)
    BoatLegacyPortalDto.ModuleDto mapModule(ServiceDefinition serviceDefinition);

    @Mapping(target = "grade", source = "spec.lintReport.grade")
    @Mapping(target = "icon", source = "spec.specType.icon")
    BoatLegacyPortalDto.SpecDto mapSpec(Spec spec);

    @Mapping(target = "portalId", source = "portal.id")
    @Mapping(target = "portalKey", source = "portal.key")
    @Mapping(target = "portalName", source = "portal.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productKey", source = "product.key")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productDescription", source = "product.content")
    @Mapping(target = "lastLintReport", ignore = true)
    @Mapping(target = "issues", ignore = true)
    BoatPortalDto mapPortal(Portal portal, Product product);


    default Map<String, BoatLegacyPortalDto.CapabilityDto> mapCapabilities(Portal portal, List<String> allEnabledTags) {
        return portal.getProducts().stream()
            .flatMap(p -> p.getCapabilities().stream())
            .map(capability -> {
                BoatLegacyPortalDto.CapabilityDto capabilityDto1 = mapCapability(capability);
                capabilityDto1.getModules().values().forEach(moduleDto -> moduleDto.setTags(moduleDto.getTags().stream().filter(allEnabledTags::contains).collect(Collectors.toSet())));
                return capabilityDto1;
            })
            .collect(Collectors.toMap(BoatLegacyPortalDto.CapabilityDto::getKey, capabilityDto -> capabilityDto));
    }

    default Map<String, BoatLegacyPortalDto.ModuleDto> mapModules(Set<ServiceDefinition> serviceDefinitions) {
        Set<String> tags = serviceDefinitions.stream().flatMap(sd -> sd.getSpecs().stream()).flatMap(spec -> spec.getTags().stream()).map(Tag::getName).collect(Collectors.toSet());

        return serviceDefinitions.stream()
            .map(serviceDefinition -> {
                BoatLegacyPortalDto.ModuleDto moduleDto = mapModule(serviceDefinition);
                moduleDto.setTags(tags);
                return moduleDto;
            })
            .collect(Collectors.toMap(BoatLegacyPortalDto.ModuleDto::getKey, moduleDto -> moduleDto));
    }


    default Map<Long, BoatLegacyPortalDto.SpecDto> map(Set<Spec> specs) {
        return specs.stream()
            .map(this::mapSpec)
            .collect(Collectors.toMap(BoatLegacyPortalDto.SpecDto::getId, specDto -> specDto));
    }


    default Map<String, Map<String, BoatLegacyPortalDto.ProductReleaseDto>> mapReleases(Portal portal) {
        Map<String, Map<String, BoatLegacyPortalDto.ProductReleaseDto>> result = new LinkedHashMap<>();
        portal.getProductReleases()
            .forEach(productRelease -> result.put(productRelease.getKey(), mapProductRelease(productRelease)));
        return result;

    }

    @NotNull
    private Map<String, BoatLegacyPortalDto.ProductReleaseDto> mapProductRelease(ProductRelease productRelease) {
        Map<String, BoatLegacyPortalDto.ProductReleaseDto> result = new LinkedHashMap<>();
        productRelease.getSpecs().stream().collect(Collectors.groupingBy(Spec::getProduct)).forEach((product, specs) -> {
            BoatLegacyPortalDto.ProductReleaseDto pr = new BoatLegacyPortalDto.ProductReleaseDto();
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

    default Map<String, BoatLegacyPortalDto.ProductDto> mapProducts(Set<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(Product::getKey, this::mapProduct));
    }

    default List<String> mapCapabilities(Set<Capability> capabilities) {
        return capabilities.stream()
            .map(Capability::getName)
            .collect(Collectors.toList());
    }


}
