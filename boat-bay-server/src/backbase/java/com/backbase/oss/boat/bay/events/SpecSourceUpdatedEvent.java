package com.backbase.oss.boat.bay.events;

import com.backbase.oss.boat.bay.domain.Source;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class SpecSourceUpdatedEvent extends ApplicationEvent {

    @Getter
    private final Source specSource;

    public SpecSourceUpdatedEvent(Object eventSource, Source specSource) {
        super(eventSource);
        this.specSource = specSource;
    }
}
