package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatService {


    private String key;
    private String name;
    private String description;
    private String icon;
    private String color;
    private LocalDateTime createdOn;
    private String createdBy;

    private BoatStatistics statistics;

}
