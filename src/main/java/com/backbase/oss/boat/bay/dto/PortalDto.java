package com.backbase.oss.boat.bay.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class PortalDto {

    private Long id;
    private String key;
    private String name;
    private String title;
    private Map<String, ProductReleaseDto> releases = new LinkedHashMap<>();
    private Map<String, ProductDto> products = new LinkedHashMap<>();
    private Map<String, CapabilityDto> capabilities = new LinkedHashMap<>();


}
