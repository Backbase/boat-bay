package com.backbase.oss.boat.bay.web.views.dashboard.v1;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    @Mapping(target = "releases", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    LegacyPortalDto mapPortal(Portal portal);

    LegacyPortalDto.ProductDto mapProduct(Product product);

    @Mapping(target = "modules", source = "serviceDefinitions")
    LegacyPortalDto.CapabilityDto mapCapability(Capability capability);

    LegacyPortalDto.PortalVersionDto mapPortalVersion(BoatPortalRepository.PortalVersion portalVersion);

    @Mapping(target = "XIcon", source = "icon")
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "tags", ignore = true)
    LegacyPortalDto.ModuleDto mapModule(ServiceDefinition serviceDefinition);

    @Mapping(target = "grade", source = "spec.lintReport.grade")
    @Mapping(target = "icon", source = "spec.specType.icon")
    LegacyPortalDto.SpecDto mapSpec(Spec spec);


    default Map<String, LegacyPortalDto.CapabilityDto> mapCapabilities(Portal portal) {
        return portal.getProducts().stream()
            .flatMap(p -> p.getCapabilities().stream())
            .map(this::mapCapability)
            .collect(Collectors.toMap(LegacyPortalDto.CapabilityDto::getKey, capabilityDto -> capabilityDto));
    }

    default Map<String, LegacyPortalDto.ModuleDto> mapModules(Set<ServiceDefinition> serviceDefinitions) {
        Set<String> tags = serviceDefinitions.stream().flatMap(sd -> sd.getSpecs().stream()).flatMap(spec -> spec.getTags().stream()).map(Tag::getName).collect(Collectors.toSet());

        return serviceDefinitions.stream()
            .map(this::mapModule)
            .map(moduleDto -> {
                moduleDto.setTags(tags);
                return moduleDto;
            })
            .collect(Collectors.toMap(LegacyPortalDto.ModuleDto::getKey, moduleDto -> moduleDto));
    }


    default Map<String, LegacyPortalDto.SpecDto> map(Set<Spec> specs) {
        return specs.stream()
            .map(this::mapSpec)
            .collect(Collectors.toMap(LegacyPortalDto.SpecDto::getName, specDto -> specDto));
    }


    default Map<String, Map<String, LegacyPortalDto.ProductReleaseDto>> mapReleases(Portal portal) {
        Map<String, Map<String, LegacyPortalDto.ProductReleaseDto>> result = new LinkedHashMap<>();
        portal.getProductReleases()
            .forEach(productRelease -> result.put(productRelease.getKey(), mapProductRelease(productRelease)));
        return result;

    }

    @NotNull
    private Map<String, LegacyPortalDto.ProductReleaseDto> mapProductRelease(ProductRelease productRelease) {
        Map<String, LegacyPortalDto.ProductReleaseDto> result = new LinkedHashMap<>();
        productRelease.getSpecs().stream().collect(Collectors.groupingBy(Spec::getProduct)).forEach((product, specs) -> {
            LegacyPortalDto.ProductReleaseDto pr = new LegacyPortalDto.ProductReleaseDto();
            pr.setKey(productRelease.getKey());
            pr.setTitle(productRelease.getName());
            pr.setServices(specs.stream().map(Spec::getServiceDefinition)
                .distinct()
                .sorted(Comparator.comparing(ServiceDefinition::getName))
                .collect(toLinkedMap(ServiceDefinition::getKey, ServiceDefinition::getName)));
            pr.setSpecs(specs.stream()
                .collect(Collectors.toMap(Spec::getKey, Spec::getVersion)));
            result.put(product.getKey(), pr);
        });
        return result;
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

    default Map<String, LegacyPortalDto.ProductDto> mapProducts(Set<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(Product::getKey, this::mapProduct));
    }

    default List<String> mapCapabilities(Set<Capability> capabilities) {
        return capabilities.stream()
            .map(Capability::getName)
            .collect(Collectors.toList());
    }
}
