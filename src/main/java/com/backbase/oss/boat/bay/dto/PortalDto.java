package com.backbase.oss.boat.bay.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class PortalDto {

    private String title;
    private Map<String, PortalDto> releases;
    private Map<String, ProductDto> products = new LinkedHashMap<>();
    private Map<String, CapabilityDto> capabilities = new LinkedHashMap<>();


}
