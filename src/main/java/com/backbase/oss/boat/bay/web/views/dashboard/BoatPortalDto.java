package com.backbase.oss.boat.bay.web.views.dashboard;

import com.backbase.oss.boat.bay.web.views.lint.BoatLintReport;
import java.util.List;
import lombok.Data;

@Data
public class BoatPortalDto {

    private String portalId;
    private String portalKey;
    private String portalName;

    private String productId;
    private String productKey;
    private String productName;

    private String productDescription;

    private BoatLintReport lastLintReport;

    private List<IssueCount> issues;

    @Data
    protected static class IssueCount {
        private String severity;
        private Long numberOfIssues;

    }
}

