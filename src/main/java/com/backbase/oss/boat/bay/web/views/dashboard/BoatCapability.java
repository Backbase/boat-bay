package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
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
