package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.LintRule;
import com.backbase.oss.boat.bay.domain.Portal;
import com.backbase.oss.boat.bay.repository.LintRuleRepository;
import java.util.Optional;
import java.util.Set;

public interface BoatLintRuleRepository extends LintRuleRepository {


    Set<LintRule> findAllByPortal(Portal portal);

    Set<LintRule> findAllByPortalAndEnabled(Portal portal, boolean enabled);


    Optional<LintRule> findByRuleId(String id);
}
