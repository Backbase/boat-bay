package com.backbase.oss.boat.bay.events;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Source;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class RuleUpdatedEvent extends ApplicationEvent {

    @Getter
    private final LintRule lintRule;

    public RuleUpdatedEvent(Object eventSource, LintRule lintRule) {
        super(eventSource);
        this.lintRule = lintRule;
    }
}
