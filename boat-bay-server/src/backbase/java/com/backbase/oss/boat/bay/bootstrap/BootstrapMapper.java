package com.backbase.oss.boat.bay.bootstrap;

import com.backbase.oss.boat.bay.config.BoatBayConfigurationProperties;
import com.backbase.oss.boat.bay.domain.Dashboard;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.SpecType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BootstrapMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "defaultPortal", ignore = true)
    Dashboard map(BoatBayConfigurationProperties.Bootstrap.Dashboard dashboard);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    SpecType map(BoatBayConfigurationProperties.Bootstrap.SpecType specType);

    @Mapping(target = "zallyConfig", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    @Mapping(target = "removeLintRule", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "linted", ignore = true)
    @Mapping(target = "lintRules", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hide", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "content", ignore = true)
    Portal map(BoatBayConfigurationProperties.Bootstrap.Dashboard.Portal portal);


    @Mapping(target = "removeProductRelease", ignore = true)
    @Mapping(target = "removeCapability", ignore = true)
    @Mapping(target = "productReleases", ignore = true)
    @Mapping(target = "portal", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "jiraProjectId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hide", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    Product map(BoatBayConfigurationProperties.Bootstrap.Dashboard.Portal.Product product);
}
