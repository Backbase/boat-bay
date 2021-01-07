package com.backbase.oss.boat.bay.mapper;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ProductRelease;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.dto.CapabilityDto;
import com.backbase.oss.boat.bay.dto.ModuleDto;
import com.backbase.oss.boat.bay.dto.PortalDto;
import com.backbase.oss.boat.bay.dto.PortalVersionDto;
import com.backbase.oss.boat.bay.dto.ProductDto;
import com.backbase.oss.boat.bay.dto.ProductReleaseDto;
import com.backbase.oss.boat.bay.dto.SpecDto;
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
    PortalDto mapPortal(Portal portal);

    ProductDto mapProduct(Product product);

    @Mapping(target = "modules", source = "serviceDefinitions")
    CapabilityDto mapCapability(Capability capability);

    PortalVersionDto mapPortalVersion(BoatPortalRepository.PortalVersion portalVersion);

    @Mapping(target = "XIcon", source = "icon")
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "tags", ignore = true)
    ModuleDto mapModule(ServiceDefinition serviceDefinition);

    @Mapping(target = "grade", source = "spec.lintReport.grade")
    @Mapping(target = "icon", source = "spec.specType.icon")
    SpecDto mapSpec(Spec spec);


    default Map<String, CapabilityDto> mapCapabilities(Portal portal) {
        return portal.getProducts().stream()
            .flatMap(p -> p.getCapabilities().stream())
            .map(this::mapCapability)
            .collect(Collectors.toMap(CapabilityDto::getKey, capabilityDto -> capabilityDto));
    }

    default Map<String, ModuleDto> mapModules(Set<ServiceDefinition> serviceDefinitions) {
        Set<String> tags = serviceDefinitions.stream().flatMap(sd -> sd.getSpecs().stream()).flatMap(spec -> spec.getTags().stream()).map(Tag::getName).collect(Collectors.toSet());

        return serviceDefinitions.stream()
            .map(this::mapModule)
            .map(moduleDto -> moduleDto.tags(tags))
            .collect(Collectors.toMap(ModuleDto::getKey, moduleDto -> moduleDto));
    }


    default Map<String, SpecDto> map(Set<Spec> specs) {
        return specs.stream()
            .map(this::mapSpec)
            .collect(Collectors.toMap(SpecDto::getName, specDto -> specDto));
    }


    default Map<String, Map<String, ProductReleaseDto>> mapReleases(Portal portal) {
        Map<String, Map<String, ProductReleaseDto>> result = new LinkedHashMap<>();
        portal.getProductReleases()
            .forEach(productRelease -> result.put(productRelease.getKey(), mapProductRelease(productRelease)));
        return result;

    }

    @NotNull
    private Map<String, ProductReleaseDto> mapProductRelease(ProductRelease productRelease) {
        Map<String, ProductReleaseDto> result = new LinkedHashMap<>();
        productRelease.getSpecs().stream().collect(Collectors.groupingBy(Spec::getProduct)).forEach((product, specs) -> {
            ProductReleaseDto pr = new ProductReleaseDto();
            pr.setKey(productRelease.getKey());
            pr.setTitle(productRelease.getName());
            pr.setServices(specs.stream().map(Spec::getServiceDefinition)
                .distinct()
                .sorted(Comparator.comparing(ServiceDefinition::getName))
                .collect(toLinkedMap(ServiceDefinition::getKey,ServiceDefinition::getName)));
            pr.setSpecs(specs.stream()
                .collect(Collectors.toMap(Spec::getKey, Spec::getVersion)));
            result.put(product.getKey(), pr);
        });
        return result;
    }

    static <T, K, U> Collector<T, ?, Map<K,U>> toLinkedMap(
        Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper)
    {
        return Collectors.toMap(
            keyMapper,
            valueMapper,
            (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            },
            LinkedHashMap::new
        );
    }

    default Map<String, ProductDto> mapProducts(Set<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(Product::getKey, this::mapProduct));
    }

    default List<String> mapCapabilities(Set<Capability> capabilities) {
        return capabilities.stream()
            .map(Capability::getName)
            .collect(Collectors.toList());
    }
}
