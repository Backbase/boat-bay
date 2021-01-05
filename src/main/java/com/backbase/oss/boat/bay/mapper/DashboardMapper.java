package com.backbase.oss.boat.bay.mapper;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.dto.CapabilityDto;
import com.backbase.oss.boat.bay.dto.DashboardDto;
import com.backbase.oss.boat.bay.dto.PortalDto;
import com.backbase.oss.boat.bay.dto.PortalVersionDto;
import com.backbase.oss.boat.bay.dto.ProductDto;
import com.backbase.oss.boat.bay.dto.ProductReleaseDto;
import com.backbase.oss.boat.bay.repository.extended.BoatPortalRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DashboardMapper {



    @Mapping(target = "releases", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    PortalDto mapPortal(Portal portal);

    ProductDto mapProduct(Product product);

    PortalVersionDto mapPortalVersion(BoatPortalRepository.PortalVersion portalVersion);

    default Map<String, CapabilityDto> mapCapabilities(Portal dashboard) {
        return new HashMap<>();
    }

    default Map<String, ProductReleaseDto> mapReleases(Portal portal) {
        return portal.getProducts().stream().map(product -> {
            ProductReleaseDto pr = new ProductReleaseDto();
            pr.setKey(product.getKey());
            pr.setTitle(product.getTitle());
            pr.setProducts(product.getCapabilities().stream()
                .flatMap(capability -> capability.getServiceDefinitions().stream())
                .flatMap(serviceDefinition -> serviceDefinition.getSpecs().stream())
                .collect(Collectors.toMap(Spec::getKey, Spec::getVersion)));
            return pr;

        }).collect(Collectors.toMap(ProductReleaseDto::getKey, productReleaseDto -> productReleaseDto));
    }

    default Map<String, ProductDto> mapProducts(Set<Product> products) {
        return products.stream().collect(Collectors.toMap(Product::getKey, this::mapProduct));
    }

    default List<String> mapCapabilities(Set<Capability> capabilities) {
        return capabilities.stream().map(Capability::getName).collect(Collectors.toList());
    }
}
