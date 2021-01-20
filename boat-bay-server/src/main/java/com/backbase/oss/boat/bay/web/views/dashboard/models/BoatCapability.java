package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BoatCapability {

    private String id;
    private String key;
    private String name;
    private String content;

    private LocalDateTime createdOn;
    private String createdBy;

    private List<BoatService> services;

    private BoatLintReport lastLintReport;

    private BoatStatistics statistics;

}
