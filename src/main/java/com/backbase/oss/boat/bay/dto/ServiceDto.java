package com.backbase.oss.boat.bay.dto;

import com.backbase.oss.boat.bay.domain.Spec;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ServiceDto {

    private String title;
    private String description;
    private List<String> versions;
    private List<String> tags;
    private String icon;

    private String grade;

    private Map<String, SpecDto> specs;

}
