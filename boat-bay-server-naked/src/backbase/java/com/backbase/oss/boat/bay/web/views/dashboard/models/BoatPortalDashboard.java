package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.backbase.oss.boat.bay.service.model.BoatLintReport;
import com.backbase.oss.boat.bay.service.model.BoatStatistics;
import lombok.Data;

@Data
public class BoatPortalDashboard {

    private String portalId;
    private String portalKey;
    private String portalName;

    private String productId;
    private String productKey;
    private String productName;

    private long numberOfServices;
    private long numberOfCapabilities;

    private String productDescription;
    private BoatLintReport lastLintReport;

    private BoatStatistics statistics;
}
