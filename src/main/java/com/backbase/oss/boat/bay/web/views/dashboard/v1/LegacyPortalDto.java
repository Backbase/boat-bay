package com.backbase.oss.boat.bay.web.views.dashboard.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
class LegacyPortalDto {

    private Long id;
    private String key;
    private String name;
    private String title;
    private Map<String, Map<String,ProductReleaseDto>> releases = new LinkedHashMap<>();
    private Map<String, ProductDto> products = new LinkedHashMap<>();
    private Map<String, CapabilityDto> capabilities = new LinkedHashMap<>();

    @Data
    static class ProductReleaseDto {
        String key;
        String title;
        Map<String, String> services;
        Map<String, String> specs;
    }

    @Data
    static class ProductDto {

        private Long id;
        private String title;
        private String key;
        private String name;
        private List<String> capabilities;

    }

    @Data
    static class PortalVersionDto {
        private Long id;
        private String name;
        private String version;

    }

    @Data
    static class CapabilityDto {

        private String key;
        private String title;
        private Map<String, ModuleDto> modules;

    }

    @Data
    static class ModuleDto {

        private String key;
        private Long id;
        private String title;
        private Map<String, String> versions;
        private String description;
        private Set<String> tags;
        @JsonProperty("x-icon")
        private String xIcon;
        private Map<String, SpecDto> specs;
    }

    @Data
    static class ServiceDto {
        private String title;
        private String description;
        private List<String> versions;
        private List<String> tags;
        private String icon;
        private String grade;
        private Map<String, SpecDto> specs;

    }

    @Data
    static class SpecDto {

        private String name;
        private String key;
        private String title;
        private String description;
        private String icon;
        private String version;
        private String grade;

    }
}
