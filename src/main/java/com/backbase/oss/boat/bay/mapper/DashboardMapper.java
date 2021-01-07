package com.backbase.oss.boat.bay.mapper;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.dto.CapabilityDto;
import com.backbase.oss.boat.bay.dto.ModuleDto;
import com.backbase.oss.boat.bay.dto.PortalDto;
import com.backbase.oss.boat.bay.dto.PortalVersionDto;
import com.backbase.oss.boat.bay.dto.ProductDto;
import com.backbase.oss.boat.bay.dto.ProductReleaseDto;
import com.backbase.oss.boat.bay.dto.SpecDto;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    default Map<String, CapabilityDto> mapCapabilities(Portal portal) {
        return portal.getProducts().stream()
            .flatMap(p -> p.getCapabilities().stream())
            .map(this::mapCapability)
            .collect(Collectors.toMap(CapabilityDto::getKey, capabilityDto -> capabilityDto));
    }

    default Map<String, ModuleDto> mapModules(Set<ServiceDefinition> serviceDefinitions) {
        return serviceDefinitions.stream()
            .map(this::mapModule)
            .collect(Collectors.toMap(ModuleDto::getKey, moduleDto -> moduleDto));
    }

    @Mapping(target = "XIcon", source = "icon")
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "tags", ignore = true)
    ModuleDto mapModule(ServiceDefinition serviceDefinition);

    default Map<String, SpecDto> map(Set<Spec> specs) {
        return specs.stream()
            .map(this::mapSpec)
            .collect(Collectors.toMap(SpecDto::getVersion, specDto -> specDto));
    }

    @Mapping(target = "grade", source = "spec.lintReport.grade")
    @Mapping(target = "icon", source = "spec.specType.icon")
    SpecDto mapSpec(Spec spec);

    default Map<String, ProductReleaseDto> mapReleases(Portal portal) {
        return portal.getProducts().stream()
            .map(this::mapProductRelease)
            .collect(Collectors.toMap(ProductReleaseDto::getKey, productReleaseDto -> productReleaseDto));
    }

    @NotNull
    private ProductReleaseDto mapProductRelease(Product product) {
        ProductReleaseDto pr = new ProductReleaseDto();
        pr.setKey(product.getKey());
        pr.setTitle(product.getTitle());
        pr.setProducts(product.getCapabilities().stream()
            .flatMap(capability -> capability.getServiceDefinitions().stream())
            .flatMap(serviceDefinition -> serviceDefinition.getSpecs().stream())
            .collect(Collectors.toMap(Spec::getKey, Spec::getVersion)));
        return pr;
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
