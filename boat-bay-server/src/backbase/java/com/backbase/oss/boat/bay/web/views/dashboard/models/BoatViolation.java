package com.backbase.oss.boat.bay.web.views.dashboard.models;

import com.fasterxml.jackson.core.JsonPointer;
import kotlin.ranges.IntRange;
import lombok.Data;
import org.zalando.zally.rule.api.Severity;

@Data
public class BoatViolation {

    private BoatLintRule rule;
    private String description;
    private Severity severity;
    private IntRange lines;
    private JsonPointer pointer;

    public String displayString() {
        return "[" + rule.getId() + "] " + severity + " - " + rule.getTitle() + ": " + description;
    }
}
