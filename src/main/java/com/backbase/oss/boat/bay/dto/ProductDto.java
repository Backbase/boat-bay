package com.backbase.oss.boat.bay.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    private String title;
    private String key;
    private String name;

    private List<String> capabilities;

}
