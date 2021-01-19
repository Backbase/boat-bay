package com.backbase.oss.boat.bay.web.views.lint;

import com.backbase.oss.boat.bay.web.views.dashboard.BoatSpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BoatLintReport {

    private Long id;

    private BoatSpec spec;

    private String name;
    private String grade;
    private boolean passed;
    private Instant lintedOn;
    private String version;

    private String openApi;
    private List<BoatViolation> violations = new ArrayList<>();

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

}
