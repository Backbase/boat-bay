package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.Capability;
import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.ServiceDefinition;
import com.backbase.oss.boat.bay.domain.Spec;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.LintRuleViolationRepository;

@SuppressWarnings("S100")
public interface BoatLintRuleViolationRepository extends LintRuleViolationRepository {
    void deleteByLintReport(LintReport lintReport);

    long countBySeverityAndLintReportSpecServiceDefinition(Severity severity, ServiceDefinition serviceDefinition);

    long countBySeverityAndLintReportSpecCapability(Severity severity, Capability capability);

    long countBySeverityAndLintReportSpecProduct(Severity severity, Product product);

    long countBySeverityAndLintReportSpec(Severity severity, Spec product);
}
