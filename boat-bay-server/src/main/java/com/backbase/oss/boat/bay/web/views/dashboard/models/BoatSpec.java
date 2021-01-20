package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatSpec {

    private long id;

    private String key;
    private String name;
    private String title;
    private String grade;
    private String version;
    private String description;
    private String icon;
    private LocalDateTime createdOn;
    private String createdBy;

    private BoatStatistics statistics;

    private boolean backwardsCompatible;
    private boolean changed;


    private BoatCapability capability;
    private BoatService serviceDefinition;

}

