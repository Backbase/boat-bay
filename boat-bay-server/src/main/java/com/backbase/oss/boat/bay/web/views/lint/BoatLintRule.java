package com.backbase.oss.boat.bay.web.views.lint;

import java.net.URI;
import lombok.Data;
import org.zalando.zally.rule.api.Severity;

@Data
public class BoatLintRule {

    private Long id;
    private String ruleId;
    private boolean enabled;
    private String title;
    private String ruleSet;
    private Severity severity;
    private URI url;

}
