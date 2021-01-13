package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.domain.Product;
import com.backbase.oss.boat.bay.domain.enumeration.Severity;
import com.backbase.oss.boat.bay.repository.LintRuleViolationRepository;

@SuppressWarnings("S100")
public interface BoatLintRuleViolationRepository extends LintRuleViolationRepository {

    void deleteByLintReport(LintReport lintReport);



    long countBySeverityAndLintReport_Spec_Product(Severity severity, Product product);

}
