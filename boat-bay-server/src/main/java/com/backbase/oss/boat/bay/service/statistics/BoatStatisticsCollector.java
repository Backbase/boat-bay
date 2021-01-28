package com.backbase.oss.boat.bay.service.statistics;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.CapabilityRepository;
import com.backbase.oss.boat.bay.repository.ProductRepository;
import com.backbase.oss.boat.bay.repository.ServiceDefinitionRepository;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.bay.repository.extended.BoatLintRuleViolationRepository;
import static com.backbase.oss.boat.bay.config.BoatCacheManager.STATISTICS;
import java.time.LocalDateTime;
import java.util.List;

import com.backbase.oss.boat.bay.service.model.BoatStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoatStatisticsCollector {

    private final List<BoatStatisticsPublisher> publisherList;

    private final BoatLintRuleViolationRepository boatLintRuleViolationRepository;
    private final ProductRepository productRepository;
    private final CapabilityRepository capabilityRepository;
    private final ServiceDefinitionRepository serviceDefinitionRepository;
    private final SpecRepository specRepository;

//    @Scheduled(fixedRateString = "PT15M")
    @Transactional
    public void publish() {
        productRepository.findAll().forEach(this::publish);
        capabilityRepository.findAll().forEach(this::publish);
        serviceDefinitionRepository.findAll().forEach(this::publish);
        specRepository.findAll().forEach(this::performCollect);
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
        BoatStatistics statistics = new BoatStatistics();
        statistics.setUpdatedOn(LocalDateTime.now());

        statistics.setMustViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecProduct(Severity.MUST, product));
        statistics.setShouldViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecProduct(Severity.SHOULD, product));
        statistics.setMayViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecProduct(Severity.MAY, product));
        statistics.setHintViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecProduct(Severity.HINT, product));

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

        statistics.setMustViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecCapability(Severity.MUST, capability));
        statistics.setShouldViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecCapability(Severity.SHOULD, capability));
        statistics.setMayViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecCapability(Severity.MAY, capability));
        statistics.setHintViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecCapability(Severity.HINT, capability));

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

        statistics.setMustViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecServiceDefinition(Severity.MUST, serviceDefinition));
        statistics.setShouldViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecServiceDefinition(Severity.SHOULD, serviceDefinition));
        statistics.setMayViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecServiceDefinition(Severity.MAY, serviceDefinition));
        statistics.setHintViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpecServiceDefinition(Severity.HINT, serviceDefinition));
        return statistics;
    }

    @Cacheable(STATISTICS)
    public BoatStatistics collect(Spec spec) {
        return performCollect(spec);
    }

    @CachePut(STATISTICS)
    public BoatStatistics performCollect(Spec spec) {
        BoatStatistics statistics = new BoatStatistics();
        statistics.setUpdatedOn(LocalDateTime.now());

        statistics.setMustViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpec(Severity.MUST, spec));
        statistics.setShouldViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpec(Severity.SHOULD, spec));
        statistics.setMayViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpec(Severity.MAY, spec));
        statistics.setHintViolationsCount(boatLintRuleViolationRepository.countBySeverityAndLintReportSpec(Severity.HINT, spec));
        return statistics;
    }


}
