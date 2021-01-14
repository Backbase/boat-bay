package com.backbase.oss.boat.bay.service.statistics;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoatStatisticsCollector {

    public static final String STATISTICS = "STATISTICS";
    private final List<BoatStatisticsPublisher> publisherList;

    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final ProductRepository productRepository;
    private final CapabilityRepository capabilityRepository;
    private final ServiceDefinitionRepository serviceDefinitionRepository;

    @Scheduled(fixedRateString = "PT15M")
    @Transactional
    public void publish() {
        productRepository.findAll().forEach(this::publish);
        capabilityRepository.findAll().forEach(this::publish);
        serviceDefinitionRepository.findAll().forEach(this::publish);

    }

    private void publish(ServiceDefinition serviceDefinition) {
        BoatStatistics boatStatistics = performCollect(serviceDefinition);
        publisherList.forEach(publisher -> {
            publisher.publish(serviceDefinition, boatStatistics);
        });

    }

    private void publish(Capability capability) {
        BoatStatistics boatStatistics = performCollect(capability);
        publisherList.forEach(publisher -> {
            publisher.publish(capability, boatStatistics);
        });
    }

    private void publish(Product product) {
        BoatStatistics boatStatistics = performCollect(product);
        publisherList.forEach(publisher -> {
            publisher.publish(product, boatStatistics);
        });
    }


    @Cacheable(STATISTICS)
    public BoatStatistics collect(Product product) {
        return performCollect(product);
    }

    @CachePut(STATISTICS)
    public BoatStatistics performCollect(Product product) {
        log.info("Collecting statistics for product: {}", product.getName());
        BoatStatistics statistics = new BoatStatistics();
        statistics.setUpdatedOn(LocalDateTime.now());

        for (Severity severity : Severity.values()) {
            BoatIssueCount issueCount = new BoatIssueCount();
            issueCount.setNumberOfIssues(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecProduct(severity, product));
            issueCount.setSeverity(severity);
            statistics.getIssues().add(issueCount);
        }

        return statistics;
    }

    @Cacheable(STATISTICS)
    public BoatStatistics collect(Capability capability) {

        return performCollect(capability);
    }

    @CachePut(STATISTICS)
    public BoatStatistics performCollect(Capability capability) {
        BoatStatistics statistics = new BoatStatistics();
        statistics.setUpdatedOn(LocalDateTime.now());

        for (Severity severity : Severity.values()) {
            BoatIssueCount issueCount = new BoatIssueCount();
            issueCount.setNumberOfIssues(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecCapability(severity, capability));
            issueCount.setSeverity(severity);
            statistics.getIssues().add(issueCount);
        }

        return statistics;
    }

    @Cacheable(STATISTICS)
    public BoatStatistics collect(ServiceDefinition serviceDefinition) {

        return performCollect(serviceDefinition);
    }

    @CachePut(STATISTICS)
    public BoatStatistics performCollect(ServiceDefinition serviceDefinition) {
        BoatStatistics statistics = new BoatStatistics();
        statistics.setUpdatedOn(LocalDateTime.now());

        for (Severity severity : Severity.values()) {
            BoatIssueCount issueCount = new BoatIssueCount();
            issueCount.setNumberOfIssues(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecServiceDefinition(severity, serviceDefinition));
            issueCount.setSeverity(severity);
            statistics.getIssues().add(issueCount);
        }

        return statistics;
    }


}
