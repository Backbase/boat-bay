package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.domain.enumeration.Changes;
import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import java.time.LocalDateTime;
import lombok.Data;
import org.openapitools.openapidiff.core.model.schema.ChangedEnum;

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

    private Changes changes;

    private BoatCapability capability;
    private BoatService serviceDefinition;

    private String openApi;
}

