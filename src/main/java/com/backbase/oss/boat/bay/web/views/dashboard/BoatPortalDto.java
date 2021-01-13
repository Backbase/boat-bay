package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
import java.util.Map;
import lombok.Data;

@Data
public class BoatPortalDto {

    private String portalId;
    private String portalKey;
    private String portalName;

    private String productId;
    private String productKey;
    private String productName;

    private BoatLintReport lastLintReport;

    private Map<Severity, Long> issues;

}

