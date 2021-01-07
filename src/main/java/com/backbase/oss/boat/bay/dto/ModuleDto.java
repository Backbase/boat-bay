package com.backbase.oss.boat.bay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class ModuleDto {

    private String key;
    private Long id;
    private String title;
    private Map<String, String> versions;
    private String description;
    private Set<String> tags;
    @JsonProperty("x-icon")
    private String xIcon;

    private Map<String, SpecDto> specs;

    public ModuleDto tags(Set<String> tags) {
        this.tags = tags;
        return this;
    }
}
