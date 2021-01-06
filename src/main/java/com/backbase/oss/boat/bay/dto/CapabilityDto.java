package com.backbase.oss.boat.bay.dto;

import com.google.common.util.concurrent.Service;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CapabilityDto {

    private String key;
    private String title;
    private Map<String, ModuleDto> modules;

}
