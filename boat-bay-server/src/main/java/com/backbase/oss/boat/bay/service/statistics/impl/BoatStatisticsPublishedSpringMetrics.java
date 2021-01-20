package com.backbase.oss.boat.bay.service.statistics.impl;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class BoatStatisticsPublishedSpringMetrics implements BoatStatisticsPublisher {

    public static final String SEVERITY = "severity";
    public static final String PORTAL = "portal";
    public static final String PRODUCT = "product";
    public static final String VIOLATIONS = "violations";
    public static final String CAPABILITY = "capability";
    public static final String SERVICE_DEFINITION = "service";

    private final MeterRegistry meterRegistry;

    @Override
    public void publish(Product product, BoatStatistics boatStatistics) {
        MultiGauge multiGauge = MultiGauge.builder("boat.product.violations")
            .tag(PORTAL, product.getPortal().getKey())
            .tag(PRODUCT, product.getKey())
            .description("Number of violations on a product")
            .baseUnit(VIOLATIONS)
            .register(meterRegistry);

        register(boatStatistics, multiGauge);
    }

    @Override
    public void publish(Capability capability, BoatStatistics boatStatistics) {

        Product product = capability.getProduct();
        MultiGauge multiGauge = MultiGauge.builder("boat.capability.violations")
            .tag(PORTAL, product.getPortal().getKey())
            .tag(PRODUCT, product.getKey())
            .tag(CAPABILITY, capability.getKey())
            .description("Number of violations on a capability")
            .baseUnit(VIOLATIONS)
            .register(meterRegistry);

        register(boatStatistics, multiGauge);
    }

    @Override
    public void publish(ServiceDefinition serviceDefinition, BoatStatistics boatStatistics) {
        Capability capability = serviceDefinition.getCapability();
        Product product = capability.getProduct();
        MultiGauge multiGauge = MultiGauge.builder("boat.service.violations")
            .tag(PORTAL, product.getPortal().getKey())
            .tag(PRODUCT, product.getKey())
            .tag(CAPABILITY, capability.getKey())
            .tag(SERVICE_DEFINITION, serviceDefinition.getKey())
            .description("Number of violations on a service")
            .baseUnit(VIOLATIONS)
            .register(meterRegistry);

        register(boatStatistics, multiGauge);

    }


    private void register(BoatStatistics boatStatistics, MultiGauge multiGauge) {
        log.debug("Publishing Micro Meter Metrics with gauge: {}", multiGauge);

        multiGauge.register(Arrays.asList(
            MultiGauge.Row.of(Tags.of(SEVERITY, Severity.MUST.name()), boatStatistics.getMustViolationsCount()),
            MultiGauge.Row.of(Tags.of(SEVERITY, Severity.MAY.name()), boatStatistics.getMustViolationsCount()),
            MultiGauge.Row.of(Tags.of(SEVERITY, Severity.SHOULD.name()), boatStatistics.getMustViolationsCount()),
            MultiGauge.Row.of(Tags.of(SEVERITY, Severity.HINT.name()), boatStatistics.getMustViolationsCount())
        ));

    }


}
