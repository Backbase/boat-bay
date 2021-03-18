package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatProduct {


    private String portalKey;
    private String portalName;

    private String id;
    private String key;
    private String name;
    private String content;

    private LocalDateTime createdOn;
    private String createdBy;

    private BoatLintReport lastLintReport;

    private BoatStatistics statistics;

    private String jiraProjectId;

}

