package com.backbase.oss.boat.bay.events;

import com.backbase.oss.boat.bay.domain.Spec;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SpecCreatedEvent extends ApplicationEvent {

    private final Spec spec;

    public SpecCreatedEvent(Object source, Spec spec) {
        super(source);
        this.spec = spec;
    }
}
