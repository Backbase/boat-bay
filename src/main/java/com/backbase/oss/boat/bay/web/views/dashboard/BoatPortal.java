package com.backbase.oss.boat.bay.web.views.dashboard;

import io.micrometer.core.instrument.Statistic;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BoatPortal {

    private String id;
    private String key;
    private String name;
    private String content;

    private LocalDateTime createdOn;
    private String createdBy;

}

