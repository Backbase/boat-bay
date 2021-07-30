package com.backbase.oss.boat.bay.web.views.dashboard.models;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatProductRelease {

    private Long id;
    private String key;
    private String name;
    private String version;
    private LocalDateTime releaseDate;
}
