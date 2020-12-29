package com.backbase.oss.boat.bay.repository.extended;

import com.backbase.oss.boat.bay.domain.LintRuleSet;
import com.backbase.oss.boat.bay.repository.LintRuleSetRepository;
import java.util.Optional;

public interface BoatLintRuleSetRepository extends LintRuleSetRepository {

    Optional<LintRuleSet> findByRuleSetId(String id);

}
