package com.backbase.oss.boat.bay.dto;

import java.util.Map;
import lombok.Data;

@Data
public class ProductReleaseDto {

    String key;
    String title;
    Map<String, String> products;
}
