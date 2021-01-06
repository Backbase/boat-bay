package com.backbase.oss.boat.bay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class ModuleDto {

    private Long id;
    private String title;
    private Map<String,String> versions;
    private String description;
    private String[] tags;
    @JsonProperty("x-icon")
    private String xIcon;

}
