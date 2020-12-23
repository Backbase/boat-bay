package com.backbase.oss.boat.bay.repository;

import com.backbase.oss.boat.bay.domain.LintRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LintRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LintRuleRepository extends JpaRepository<LintRule, Long> {
}
