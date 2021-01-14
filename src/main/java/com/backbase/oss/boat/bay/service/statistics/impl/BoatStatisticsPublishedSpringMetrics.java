package com.backbase.oss.boat.bay.service.statistics.impl;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsPublisher;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class BoatStatisticsPublishedSpringMetrics implements BoatStatisticsPublisher {

    private final MeterRegistry meterRegistry;

    @Override
    public void publish(Product product, BoatStatistics boatStatistics) {
        MultiGauge multiGauge = MultiGauge.builder("boat.product.violations")
            .tag("portal", product.getPortal().getKey())
            .tag("product", product.getKey())
            .description("Number of violations on a product")
            .baseUnit("violations")
            .register(meterRegistry);

        register(boatStatistics, multiGauge);
    }

    @Override
    public void publish(Capability capability, BoatStatistics boatStatistics) {

        Product product = capability.getProduct();
        MultiGauge multiGauge = MultiGauge.builder("boat.capability.violations")
            .tag("portal", product.getPortal().getKey())
            .tag("product", product.getKey())
            .tag("capability", capability.getKey())
            .description("Number of violations on a capability")
            .baseUnit("violations")
            .register(meterRegistry);

        register(boatStatistics, multiGauge);
    }

    @Override
    public void publish(ServiceDefinition serviceDefinition, BoatStatistics boatStatistics) {
        Capability capability = serviceDefinition.getCapability();
        Product product = capability.getProduct();
        MultiGauge multiGauge = MultiGauge.builder("boat.service.violations")
            .tag("portal", product.getPortal().getKey())
            .tag("product", product.getKey())
            .tag("capability", capability.getKey())
            .tag("serviceDefinition", serviceDefinition.getKey())
            .description("Number of violations on a capability")
            .baseUnit("violations")
            .register(meterRegistry);

        register(boatStatistics, multiGauge);

    }


    private void register(BoatStatistics boatStatistics, MultiGauge multiGauge) {
        log.info("Publishing Micro Meter Metrics with gauge: {}", multiGauge);

        List<MultiGauge.Row<?>> rows = boatStatistics.getIssues().stream()
            .map(boatIssueCount -> {
                String severityName = boatIssueCount.getSeverity().name();
                Tags tag = Tags.of("severity", severityName);
                return MultiGauge.Row.of(tag, boatIssueCount.getNumberOfIssues());
            })
            .collect(Collectors.toList());

        multiGauge.register(rows);
    }


}
