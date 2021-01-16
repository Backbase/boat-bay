package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatSpec {

    private String key;
    private String name;
    private String version;
    private String description;
    private String icon;
    private LocalDateTime createdOn;
    private String createdBy;

    private BoatStatistics statistics;

}

