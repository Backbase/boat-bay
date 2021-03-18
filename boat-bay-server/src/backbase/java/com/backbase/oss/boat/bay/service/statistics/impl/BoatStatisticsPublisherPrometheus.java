package com.backbase.oss.boat.bay.service.statistics.impl;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.service.statistics.BoatStatistics;
import com.backbase.oss.boat.bay.service.statistics.BoatStatisticsPublisher;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
@ConditionalOnBean(PushGateway.class)
public class BoatStatisticsPublisherPrometheus implements BoatStatisticsPublisher {

    private final PushGateway pushGateway;

    @Override
    public void publish(Product product, BoatStatistics boatStatistics) {

        CollectorRegistry registry = new CollectorRegistry();

        Gauge productGauge = Gauge.build()
            .name(product.getPortal().getKey() + "_" + product.getKey() + "_violations")
            .help("Linting Violations for all API's in + " + product.getName())
            .register(registry);

        push(boatStatistics, registry, productGauge);
    }

    @Override
    public void publish(Capability capability, BoatStatistics boatStatistics) {
        CollectorRegistry registry = new CollectorRegistry();

        Product product = capability.getProduct();
        Gauge productGauge = Gauge.build()
            .name(product.getPortal().getKey() + "_" + product.getKey() + "_" + capability.getKey() + "_violations")
            .help("Linting Violations for all API's in + " + capability.getName())
            .register(registry);

        push(boatStatistics, registry, productGauge);
    }

    @Override
    public void publish(ServiceDefinition serviceDefinition, BoatStatistics boatStatistics) {
        CollectorRegistry registry = new CollectorRegistry();

        Product product = serviceDefinition.getCapability().getProduct();
        Capability capability = serviceDefinition.getCapability();

        Gauge productGauge = Gauge.build()
            .name(product.getPortal().getKey() + "_" + product.getKey() + "_" + capability.getKey() + "_" + serviceDefinition.getKey() + "_violations")
            .help("Linting Violations for all API's in + " + product.getName())
            .register(registry);

        push(boatStatistics, registry, productGauge);
    }

    private void push(BoatStatistics boatStatistics, CollectorRegistry registry, Gauge productGauge) {
        productGauge.setToCurrentTime();
        productGauge.labels(Severity.MUST.name()).set(boatStatistics.getMustViolationsCount());
        productGauge.labels(Severity.SHOULD.name()).set(boatStatistics.getShouldViolationsCount());
        productGauge.labels(Severity.MAY.name()).set(boatStatistics.getMayViolationsCount());
        productGauge.labels(Severity.HINT.name()).set(boatStatistics.getHintViolationsCount());

        try {
            pushGateway.pushAdd(registry, "boat");
        } catch (IOException e) {
            log.warn("Failed to push metrics");
        }
    }


}
