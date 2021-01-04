package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.LintReport;
import com.backbase.oss.boat.bay.repository.LintRuleViolationRepository;

public interface BoatLintRuleViolationRepository extends LintRuleViolationRepository {

    void deleteByLintReport(LintReport lintReport);


}
