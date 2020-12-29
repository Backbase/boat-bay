package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import java.util.Optional;

public interface BoatLintRuleRepository extends LintRuleRepository {

    Optional<LintRule> findByRuleId(String ruleId);
}
